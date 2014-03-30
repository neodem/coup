package com.neodem.coup.common.messaging;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.neodem.coup.common.game.cards.CoupCard;
import com.neodem.coup.common.game.cards.CoupCardType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Author: Vincent Fumo (vfumo) : vincent_fumo@cable.comcast.com
 * Created Date: 3/30/14
 */
public class JsonMessageTranslatorTest {

    private JsonMessageTranslator t;

    @Before
    public void before() {
        t = new JsonMessageTranslator();
    }

    @After
    public void after() {
        t = null;
    }

    @Test
    public void marshallingOfCardCollectionShouldWorkCorrectly() throws Exception {

        Multiset<CoupCard> cardCollection = HashMultiset.create();
        cardCollection.add(new CoupCard(1, CoupCardType.Ambassador, false));
        cardCollection.add(new CoupCard(2, CoupCardType.Ambassador, false));

        String m = t.marshalMessage(MessageType.actionHappened, cardCollection);

        Multiset<CoupCard> result = t.unmarshalCardMultisetFromMessage(m);

        assertThat(result, equalTo(cardCollection));

    }
}
