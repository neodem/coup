package com.neodem.coup.serverside.actionProcessors;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.neodem.coup.cards.CoupCard;
import com.neodem.coup.players.CoupPlayer;
import com.neodem.coup.serverside.ServerSideGameContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/25/14
 */
public class ExchangeActionProcessorTest {
    private ServerSideGameContext context;
    private ExchangeActionProcessor processor;
    private CoupPlayer mockPlayer;

    @Before
    public void before() {
        mockPlayer = mock(CoupPlayer.class);

        context = new ServerSideGameContext();
        context.addPlayer(mockPlayer);

        processor = new ExchangeActionProcessor(context);


    }

    @After
    public void after() {
        mockPlayer = null;
        processor = null;
        context = null;
    }

    @Test
    public void isReturnedCollectionOkShouldFailIfWeTryToReturnSomethingThatWasntInTheHand() {
        Multiset<CoupCard> handCards = HashMultiset.create();
        handCards.add(CoupCard.Ambassador);
        handCards.add(CoupCard.Captain);
        handCards.add(CoupCard.Assasin);
        handCards.add(CoupCard.Contessa);

        Multiset<CoupCard> returnedCards = HashMultiset.create();
        returnedCards.add(CoupCard.Ambassador);
        returnedCards.add(CoupCard.Duke);

        assertThat(processor.isReturnedCollectionOk(handCards, returnedCards), is(false));
    }

    @Test
    public void isReturnedCollectionOkShouldPassIfWeTryToReturnSomethingThatIsInTheHand() {
        Multiset<CoupCard> handCards = HashMultiset.create();
        handCards.add(CoupCard.Ambassador);
        handCards.add(CoupCard.Captain);
        handCards.add(CoupCard.Assasin);
        handCards.add(CoupCard.Contessa);

        Multiset<CoupCard> returnedCards = HashMultiset.create();
        returnedCards.add(CoupCard.Ambassador);
        returnedCards.add(CoupCard.Assasin);

        assertThat(processor.isReturnedCollectionOk(handCards, returnedCards), is(true));
    }

    @Test
    public void isReturnedCollectionOkShouldFailIfWeReturnDups() {
        Multiset<CoupCard> handCards = HashMultiset.create();
        handCards.add(CoupCard.Ambassador);
        handCards.add(CoupCard.Captain);
        handCards.add(CoupCard.Assasin);
        handCards.add(CoupCard.Contessa);

        Multiset<CoupCard> returnedCards = HashMultiset.create();
        returnedCards.add(CoupCard.Ambassador);
        returnedCards.add(CoupCard.Ambassador);

        assertThat(processor.isReturnedCollectionOk(handCards, returnedCards), is(true));
    }


}
