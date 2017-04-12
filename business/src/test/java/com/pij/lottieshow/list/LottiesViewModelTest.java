package com.pij.lottieshow.list;

import com.pij.lottieshow.interactor.LottieSink;
import com.pij.lottieshow.interactor.LottieSource;
import com.pij.lottieshow.model.LottieFile;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.util.List;

import rx.observers.TestSubscriber;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static rx.Observable.just;

/**
 * <p>Created on 09/04/2017.</p>
 * @author Pierrejean
 */

public class LottiesViewModelTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    LottieSink mockSink;
    @Mock
    LottieSource mockSource;
    @InjectMocks
    LottiesViewModel sut;

    @Test
    public void sourceEmptyListEmitEmptyList() {
        when(mockSource.getLottieFiles()).thenReturn(just(emptyList()));
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.shouldShowList().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void sourceSingleItemEmitSingletonList() {
        LottieFile lottie = LottieFile.create(new File("parent", "label"));
        when(mockSource.getLottieFiles()).thenReturn(just(singletonList(lottie)));
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.shouldShowList().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(singletonList(LottieFile.create(new File("parent", "label"))));
    }

    @Test
    public void twoItemsSourceEmit2ItemList() {
        LottieFile lottie1 = LottieFile.create(new File("parent1", "label1"));
        LottieFile lottie2 = LottieFile.create(new File("parent2", "label2"));
        when(mockSource.getLottieFiles()).thenReturn(just(asList(lottie1, lottie2)));
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.shouldShowList().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(asList(LottieFile.create(new File("parent1", "label1")),
                                      LottieFile.create(new File("parent2", "label2"))));
    }

}
