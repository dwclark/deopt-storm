import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

final int THREADS = args[0].toInteger();
final int ITERATIONS = args[1].toInteger();

final AtomicReference<StateId> ref = new AtomicReference();
final AtomicReference<Object> val = new AtomicReference();

println("Attach tools now, then hit enter...");
System.in.read();
println("deopt-storm beginning now...");

def threads = (0..<THREADS).collect { num ->
    Thread.start "deopt-${num}", {
        List<State> myChain = new ArrayList();
        def finder = { it.select(); };
        (0..<ITERATIONS).each {
            int length = ThreadLocalRandom.current().nextInt(1, State.MAX_LENGTH);
            State state = State.chain(length, myChain).find(finder);

            //force the jvm to track outcome of execute()
            StateId id = state.execute();
            ref.set(id);
            val.set(id.closure.call());
        }
    }
}

threads.each { it.join(); }

println("Final state: ${ref}");
println("Final val: ${val}");
