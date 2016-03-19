import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

final int THREADS = args[0].toInteger();
final int ITERATIONS = args[1].toInteger();

final AtomicReference<StateId> ref = new AtomicReference();
final AtomicReference<Object> val = new AtomicReference();

System.out.println("Attach tools now, then hit enter...");
System.in.read();
System.out.println("deopt-storm beginning now...");

def threads = (0..<THREADS).collect { num ->
    Thread.start "deopt-${num}", {
        (0..<ITERATIONS).each {
            List<State> myChain = State.chain(ThreadLocalRandom.current().nextInt(1, State.MAX_LENGTH));
            State state = myChain.find { c -> c.select(); };

            //force the jvm to track outcome of execute()
            StateId id = state.execute();
            ref.set(id);
            val.set(id.closure.call());
        }
    }
}

threads.each { it.join(); }

System.out.println("Final state: ${ref}");
System.out.println("Final val: ${val}");
