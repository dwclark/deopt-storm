import java.util.concurrent.ThreadLocalRandom;

trait Even {
    boolean select() { return ThreadLocalRandom.current().nextBoolean(); }
}

trait LessLikely {
    boolean select() { return ThreadLocalRandom.current().nextInt(0, 5) == 0; }
}

trait Rare {
    boolean select() { return ThreadLocalRandom.current().nextInt(0, 20) == 0; }
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

class Shuffled {
    final static ThreadLocal<List<Integer>> SHORT = new ThreadLocal<List<Integer>>() {
        @Override public List<Integer> initialValue() {
            return (1..10).collect { it; };
        }
    }

    final static ThreadLocal<List<Integer>> MEDIUM = new ThreadLocal<List<Integer>>() {
        @Override public List<Integer> initialValue() {
            return (11..35).collect { it; };
        }
    }

    final static ThreadLocal<List<Integer>> LONG = new ThreadLocal<List<Integer>>() {
        @Override public List<Integer> initialValue() {
            return (36..50).collect { it; };
        }
    }
}

trait Shuffle_1_10 {
    
    StateId execute() {
        Collections.shuffle(Shuffled.SHORT.get());
        sleep(Shuffled.SHORT.get().get(0).longValue());
        return StateId.random();
    }
}

trait Shuffle_11_35 {
    StateId execute() {
        Collections.shuffle(Shuffled.MEDIUM.get());
        sleep(Shuffled.MEDIUM.get().get(0).longValue());
        return StateId.random();
    }
}

trait Shuffle_36_50 {
    StateId execute() {
        Collections.shuffle(Shuffled.LONG.get());
        sleep(Shuffled.LONG.get().get(0).longValue());
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
    static final State GUARANTEED = new Guaranteed();
    
    static List<State> chain(final int length, final List<State> states) {
        assert(length > 0);
        assert(length <= MAX_LENGTH);
        states.clear();
        for(int i = 0; i < length; ++i) {
            states.add(INDEFINITE[ThreadLocalRandom.current().nextInt(0, INDEFINITE.size())]);
        }

        Collections.shuffle(states);
        states.add(GUARANTEED);
        return states;
    }
}
