import java.util.concurrent.ThreadLocalRandom;

trait Even {
    boolean select() { return ThreadLocalRandom.current().nextBoolean(); }
}

trait LessLikely {
    boolean select() { return ThreadLocalRandom.current().nextInt(0, 5); }
}

trait Rare {
    boolean select() { return ThreadLocalRandom.current().nextInt(0, 20); }
}

trait Wait_1_10 {
    StateId execute() {
        sleep(nextInt(1, 10));
        return StateId.random();
    }
}

trait Wait_11_35 {
    StateId execute() {
        sleep(nextInt(11, 35));
        return StateId.random();
    }
}

trait Wait_36_50 {
    StateId execute() {
        sleep(nextInt(36, 50));
        return StateId.random();
    }
}

trait Shuffle_1_10 {
    StateId execute() {
        List waits = (1..10).collect { it; };
        Collections.shuffle(waits);
        sleep(waits[0]);
        return StateId.random();
    }
}

trait Shuffle_11_35 {
    StateId execute() {
        List waits = (11..35).collect { it; };
        Collections.shuffle(waits);
        sleep(waits[0]);
        return StateId.random();
    }
}

trait Shuffle_36_50 {
    StateId execute() {
        List waits = (36..50).collect { it; };
        Collections.shuffle(waits);
        sleep(waits[0]);
        return StateId.random();
    }
}

abstract class State {

    static final int nextInt(int lower, int upper) {
        ThreadLocalRandom.current().nextInt(lower, upper);
    }
    
    abstract boolean select();
    abstract StateId execute();

    static class EvenWait_1_10 extends State implements Even, Wait_1_10 { }
    static class EvenWait_11_35 extends State implements Even, Wait_11_35 { }
    static class EvenWait_36_50 extends State implements Even, Wait_36_50 { }
    static class EvenShuffle_1_10 extends State implements Even, Shuffle_1_10 { }
    static class EvenShuffle_11_35 extends State implements Even, Shuffle_11_35 { }
    static class EvenShuffle_36_50 extends State implements Even, Shuffle_36_50 { }

    static class LessLikelyWait_1_10 extends State implements LessLikely, Wait_1_10 { }
    static class LessLikelyWait_11_35 extends State implements LessLikely, Wait_11_35 { }
    static class LessLikelyWait_36_50 extends State implements LessLikely, Wait_36_50 { }
    static class LessLikelyShuffle_1_10 extends State implements LessLikely, Shuffle_1_10 { }
    static class LessLikelyShuffle_11_35 extends State implements LessLikely, Shuffle_11_35 { }
    static class LessLikelyShuffle_36_50 extends State implements LessLikely, Shuffle_36_50 { }

    static class RareWait_1_10 extends State implements Rare, Wait_1_10 { }
    static class RareWait_11_35 extends State implements Rare, Wait_11_35 { }
    static class RareWait_36_50 extends State implements Rare, Wait_36_50 { }
    static class RareShuffle_1_10 extends State implements Rare, Shuffle_1_10 { }
    static class RareShuffle_11_35 extends State implements Rare, Shuffle_11_35 { }
    static class RareShuffle_36_50 extends State implements Rare, Shuffle_36_50 { }

    static class Guaranteed extends State {
        boolean select() { true; }
        StateId execute() {
            sleep(nextInt(1, 50));
            return StateId.random();
        }
    }

    static final private INDEFINITE = [
        new EvenWait_1_10(), new EvenWait_11_35(), new EvenWait_36_50(),
        new EvenShuffle_1_10(), new EvenShuffle_11_35(), new EvenShuffle_36_50(),

        new LessLikelyWait_1_10(), new LessLikelyWait_11_35(), new LessLikelyWait_36_50(),
        new LessLikelyShuffle_1_10(), new LessLikelyShuffle_11_35(), new LessLikelyShuffle_36_50(),

        new RareWait_1_10(), new RareWait_11_35(), new RareWait_36_50(),
        new RareShuffle_1_10(), new RareShuffle_11_35(), new RareShuffle_36_50() ].asImmutable();
    
    static final int MAX_LENGTH = INDEFINITE.size() + 1;
    
    static List<State> chain(final int length) {
        assert(length > 0);
        assert(length <= MAX_LENGTH);
        List<State> toRandomize = new ArrayList(INDEFINITE);
        Collections.shuffle(toRandomize);
        List<State> toReturn = new ArrayList(length);
        (0..<(length-1)).each { num -> toReturn.add(toRandomize[num]); };
        toReturn.add(new Guaranteed());
        return toReturn;
    }
}
