package com.pij.lottieshow.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import com.jakewharton.rxbinding.view.RxView.clicks
import com.pij.lottieshow.R
import com.pij.lottieshow.detail.LottieActivity
import com.pij.lottieshow.detail.LottieFragment
import com.pij.lottieshow.model.Converter
import com.pij.lottieshow.model.LottieContent
import com.pij.lottieshow.model.LottieFile
import com.pij.lottieshow.model.LottieUi
import com.pij.lottieshow.saf.SafClient
import com.pij.lottieshow.ui.LibraryString
import com.pij.lottieshow.ui.Utils
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_lottie_list.*
import kotlinx.android.synthetic.main.lottie_list.*
import org.apache.commons.lang3.tuple.Pair
import rx.Observable
import rx.Observable.empty
import rx.Observable.just
import rx.Single
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * An activity representing a list of Lotties. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [LottieActivity] representing
 * item label. On tablets, the activity presents the list of items and
 * item label side-by-side using two vertical panes.
 */
class LottiesActivity : DaggerAppCompatActivity() {

    private val subscriptions = CompositeSubscription()
    private val onFirstCreate = BehaviorSubject.create<Boolean>()
    private lateinit var progress: Snackbar

    // by bindView(R.id.lottie_detail_container)
    private var detailContainer: View? = null
    @Inject
    lateinit var viewModel: LottiesViewModel
    @Inject
    lateinit var saf: SafClient
    @Inject
    lateinit var converter: Converter
    @Inject
    lateinit var libraryString: LibraryString

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie_list)

        setSupportActionBar(toolbar)

        progress = Snackbar.make(fab, R.string.loading, Snackbar.LENGTH_INDEFINITE)

        val adapter = LottiesAdapter(R.layout.lottie_list_item)
        list.adapter = adapter

        subscriptions.addAll(
                clicks(fab).subscribe({ _ -> pickJsonFile() }, this::notifyError),
                saf.analysed().subscribe(viewModel::addLottie, this::notifyError),
                saf.inProgress().filter { it }.subscribe({ _ -> progress.show() }, this::notifyError),
                saf.inProgress().filter { !it }.subscribe({ _ -> progress.dismiss() }, {}),

                adapter.itemClicked().flatMapSingle { this.toModel(it) }.subscribe(viewModel::select,
                                                                                   this::notifyError),

                // error handling
                viewModel.shouldShowList().subscribe({}, this::notifyError),
                viewModel.shouldShowList()
                        .flatMap { Observable.from(it).flatMapSingle { fromModel(it) }.toList() }
                        .subscribe(adapter::setItems, {}),

                // The detail container view will be present only in the
                // large-screen layouts (res/values-w900dp).
                // If this view is present, then the
                // activity should be in two-pane mode.
                if (detailContainer == null)
                    viewModel.shouldShowLottie().compose(mapWithoutError({ LottieActivity.createIntent(this, it) }))
                            .subscribe(this::startActivity, this::notifyError)
                else
                    viewModel.shouldShowLottie().compose(mapWithoutError({ LottieFragment.createInstance(it) }))
                            .subscribe(this::setDetailFragment, this::notifyError),

                viewModel.shouldShowList()
                        .map { it.isEmpty() }
                        .map { empty -> if (empty) View.VISIBLE else View.GONE }
                        .subscribe({ empty.visibility = it }, {}),

                Observable.zip(viewModel.shouldShowList().map { it.isEmpty() },
                               onFirstCreate)
                { emptyList, firstCreate -> emptyList && firstCreate }
                        .filter { it }
                        .take(1)
                        .subscribe({ this.animateFab(extraordinary = true) }, {})
        )

        onFirstCreate.onNext(savedInstanceState == null)
        println("PJC in onFirstCreate")
    }

    override fun onDestroy() {
        fab.clearAnimation()
        subscriptions.clear()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            REQUEST_CODE_PICK -> when (resultCode) {
                RESULT_OK -> saf.analyse(data)
                else -> {
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * Put the library version in the menu.
     * We don't do anything when the _only_ menu is clicked.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.lotties_menu, menu)
        libraryString.configure(menu)
        return true
    }

    private fun animateFab(extraordinary: Boolean) {
        val resource = if (extraordinary) R.anim.pulses else R.anim.slide_in_right
        val animation = AnimationUtils.loadAnimation(this, resource)
        fab.startAnimation(animation)
    }

    private fun toModel(ui: LottieUi): Single<LottieFile> {
        return converter.toModel(ui)
    }

    private fun fromModel(model: LottieFile): Single<Pair<LottieUi, LottieContent>> {
        return Single.zip(converter.fromModel(model),
                          Single.just(model).map { LottieContent.create(it.content) })
        { ui, content -> Pair.of(ui, content) }
    }

    private fun notifyError(error: Throwable) {
        Utils.notifyError(error, fab)
    }

    private fun pickJsonFile() {
        saf.pickJsonFile(this, REQUEST_CODE_PICK)
    }

    private fun <T> mapWithoutError(mapper: (LottieUi) -> T): Observable.Transformer<LottieFile, T> {
        return Observable.Transformer<LottieFile, T> { lottie ->
            lottie.flatMapSingle { fromModel(it) }
                    .map { it -> it.left }
                    .flatMap { item ->
                        just(item).map(mapper)
                                .doOnError { this::notifyError }
                                .onErrorResumeNext(empty<T>())
                    }
        }
    }

    private fun setDetailFragment(fragment: LottieFragment) {
        supportFragmentManager.beginTransaction().replace(R.id.detail_container, fragment).commit()
    }

    companion object {

        private val REQUEST_CODE_PICK = 24

        fun createIntent(context: Context): Intent {
            return Intent(context, LottiesActivity::class.java)
        }
    }
}
