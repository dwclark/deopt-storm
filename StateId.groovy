import java.util.concurrent.ThreadLocalRandom;

enum StateId {

    _1(1, { -> Boolean.valueOf(tlr.nextBoolean()) }),
    _2(2, { -> toShort(tlr.nextInt()) }),
    _3(3, { -> toShort((int) tlr.nextFloat()) }),
    _4(4, { -> toShort((int) tlr.nextLong()) }),
    _5(5, { -> toShort((int) tlr.nextDouble()) }),
    _6(6, { -> toShort((int) tlr.nextGaussian()); }),
    _7(7, { -> Boolean.valueOf(!tlr.nextBoolean()) }),
    _8(8, { -> toInteger(tlr.nextInt()) }),
    _9(9, { -> toInteger((int) tlr.nextFloat()) }),
    _10(10, { -> toInteger((int) tlr.nextLong()) }),
    _11(11, { -> toInteger((int) tlr.nextDouble()) }),
    _12(12, { -> toInteger((int) tlr.nextGaussian()); }),
    _13(13, { -> toString((int) tlr.nextGaussian()) }),
    _14(14, { -> toString((int) System.currentTimeMillis()) }),
    _15(15, { -> toString((int) System.nanoTime()) }),
    _16(16, { -> toString(tlr.nextInt()) }),
    _17(17, { -> toString((int) tlr.nextFloat()) }),
    _18(18, { ->  PI }),
    _19(19, { -> E }),
    _20(20, { -> 'constant string' }),
    _21(21, { -> 'another constant string' });

    private StateId(final int id, final Closure closure) {
        this.id = id;
        this.closure = closure;
    }
    
    final int id;
    final Closure closure;
    
    private static final Map<Integer,StateId> ALL = values().inject([:]) { m, s -> m[s.id] = s; m; }.asImmutable();
    
    static StateId random() {
        def tlr = ThreadLocalRandom.current();
        Integer key = Integer.valueOf(tlr.nextInt(1, ALL.size() + 1));
        return ALL[key]
    }

    static ThreadLocalRandom getTlr() {
        return ThreadLocalRandom.current();
    }

    static Short toShort(final int val) {
        return Short.valueOf((short) ((Math.abs(val) % 256) - tlr.nextInt(0, 257)));
    }

    static Integer toInteger(final int val) {
        return Integer.valueOf((short) ((Math.abs(val) % 256) - tlr.nextInt(0, 257)));
    }

    private static final List<String> STRINGS = [ 'asdfasdf', 'ewrgerf', 'dsfghrsgdfh', '3245345', ';asdffads',
                                                  'fghjjhfg', '__________', '234234', '!@#$%^%', 'IJTYHGHrgg',
                                                  'ASDFASDF', 'EWRGERF', 'DSFGHRSGDFH', '3245345', ';ASDFFADS',
                                                  '123', '456', '789', 'i', '2', '3' ].asImmutable();

    static String toString(final int val) {
        int idx = Math.abs(val) % STRINGS.size();
        return STRINGS.get(idx);
    }

    static final Double PI = Math.PI;
    static final Double E = Math.E;
}
