package com.neodem.coup.server.game.actionProcessors;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.CoupCommunicationInterface;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.server.game.ServerSideGameContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.neodem.coup.common.game.cards.CoupCardType.Ambassador;
import static com.neodem.coup.common.game.cards.CoupCardType.Assasin;
import static com.neodem.coup.common.game.cards.CoupCardType.Captain;
import static com.neodem.coup.common.game.cards.CoupCardType.Contessa;
import static com.neodem.coup.common.game.cards.CoupCardType.Duke;
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
    private CoupCommunicationInterface mockPlayer;

    @Before
    public void before() {
        mockPlayer = mock(CoupCommunicationInterface.class);

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
        handCards.add(new CoupCard(Ambassador));
        handCards.add(new CoupCard(Captain));
        handCards.add(new CoupCard(Assasin));
        handCards.add(new CoupCard(Contessa));

        Multiset<CoupCard> returnedCards = HashMultiset.create();
        returnedCards.add(new CoupCard(Ambassador));
        returnedCards.add(new CoupCard(Duke));

        assertThat(processor.isReturnedCollectionOk(handCards, returnedCards), is(false));
    }

    @Test
    public void isReturnedCollectionOkShouldPassIfWeTryToReturnSomethingThatIsInTheHand() {
        Multiset<CoupCard> handCards = HashMultiset.create();
        handCards.add(new CoupCard(Ambassador));
        handCards.add(new CoupCard(Captain));
        handCards.add(new CoupCard(Assasin));
        handCards.add(new CoupCard(Contessa));

        Multiset<CoupCard> returnedCards = HashMultiset.create();
        returnedCards.add(new CoupCard(Ambassador));
        returnedCards.add(new CoupCard(Assasin));

        assertThat(processor.isReturnedCollectionOk(handCards, returnedCards), is(true));
    }

    @Test
    public void isReturnedCollectionOkShouldFailIfWeReturnDups() {
        Multiset<CoupCard> handCards = HashMultiset.create();
        handCards.add(new CoupCard(Ambassador));
        handCards.add(new CoupCard(Captain));
        handCards.add(new CoupCard(Assasin));
        handCards.add(new CoupCard(Contessa));

        Multiset<CoupCard> returnedCards = HashMultiset.create();
        returnedCards.add(new CoupCard(Ambassador));
        returnedCards.add(new CoupCard(Ambassador));

        assertThat(processor.isReturnedCollectionOk(handCards, returnedCards), is(false));
    }

    @Test
    public void isReturnedCollectionOkShouldPassIfWeReturnDupsAndDupsAreIn() {
        Multiset<CoupCard> handCards = HashMultiset.create();
        handCards.add(new CoupCard(Ambassador));
        handCards.add(new CoupCard(Ambassador));
        handCards.add(new CoupCard(Assasin));
        handCards.add(new CoupCard(Contessa));

        Multiset<CoupCard> returnedCards = HashMultiset.create();
        returnedCards.add(new CoupCard(Ambassador));
        returnedCards.add(new CoupCard(Ambassador));

        assertThat(processor.isReturnedCollectionOk(handCards, returnedCards), is(true));
    }
}
