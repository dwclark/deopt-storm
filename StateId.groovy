import java.util.concurrent.ThreadLocalRandom;

enum StateId {

    _1({ -> ThreadLocalRandom.current().nextBoolean() }),
    _2({ -> ThreadLocalRandom.current().nextInt() }),
    _3({ -> ThreadLocalRandom.current().nextFloat() }),
    _4({ -> ThreadLocalRandom.current().nextLong() }),
    _5({ -> ThreadLocalRandom.current().nextDouble() }),
    _6({ -> ThreadLocalRandom.current().nextGaussian(); }),
    _7({ -> def b = new byte[10]; ThreadLocalRandom.current().nextBytes(b); return b; }),
    _8({ -> !ThreadLocalRandom.current().nextBoolean() }),
    _9({ -> ThreadLocalRandom.current().nextInt() * ThreadLocalRandom.current().nextInt() }),
    _10({ -> ThreadLocalRandom.current().nextFloat() * ThreadLocalRandom.current().nextFloat() }),
    _11({ -> ThreadLocalRandom.current().nextLong() * ThreadLocalRandom.current().nextLong() }),
    _12({ -> ThreadLocalRandom.current().nextDouble() * ThreadLocalRandom.current().nextDouble() }),
    _13({ -> ThreadLocalRandom.current().nextGaussian()* ThreadLocalRandom.current().nextGaussian() }),
    _14({ -> System.currentTimeMillis() }),
    _15({ -> System.nanoTime() }),
    _16({ -> new BigInteger(System.currentTimeMillis()) }),
    _17({ -> new BigInteger(System.nanoTime()) }),
    _18({ -> new Date() }),
    _19({ -> 'constant string' }),
    _20({ -> def l = System.currentTimeMillis(); "current time: ${l}" });
    
    private StateId(final Closure closure) {
        this.closure = closure;
    }
    
    static StateId byId(int id) {
        return valueOf("_${id}");
    }
    
    final Closure closure;
    
    static final int MIN = 1;
    static final int MAX = 20;

    static StateId random() {
        byId(ThreadLocalRandom.current().nextInt(1, MAX + 1));
    }
}
