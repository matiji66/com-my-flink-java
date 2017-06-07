package demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.checkpoint.CheckpointedRestoring;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class BufferingSink implements SinkFunction<Tuple2<String, Integer>>,
		CheckpointedFunction,
		CheckpointedRestoring<ArrayList<Tuple2<String, Integer>>> {

	private final int threshold;

	private transient ListState<Tuple2<String, Integer>> checkpointedState;

	private List<Tuple2<String, Integer>> bufferedElements;

	public BufferingSink(int threshold) {
		this.threshold = threshold;
		this.bufferedElements = new ArrayList<>();
	}

	@Override
	public void invoke(Tuple2<String, Integer> value) throws Exception {
		bufferedElements.add(value);
		if (bufferedElements.size() == threshold) {
			for (Tuple2<String, Integer> element : bufferedElements) {
				// send it to the sink
			}
			bufferedElements.clear();
		}
	}

	@Override
	public void snapshotState(FunctionSnapshotContext context) throws Exception {
		checkpointedState.clear();
		for (Tuple2<String, Integer> element : bufferedElements) {
			checkpointedState.add(element);
		}
	}

	@Override
	public void initializeState(FunctionInitializationContext context)
			throws Exception {
 		
		ListStateDescriptor<Tuple2<String, Integer>> descriptor = new ListStateDescriptor<>(
				"buffered-elements",
				TypeInformation.of(new TypeHint<Tuple2<String, Integer>>() {}));

		checkpointedState = context.getOperatorStateStore().getListState(descriptor);

		if (context.isRestored()) {
			for (Tuple2<String, Integer> element : checkpointedState.get()) {
				bufferedElements.add(element);
			}
		}
	}

	@Override
	public void restoreState(ArrayList<Tuple2<String, Integer>> state)
			throws Exception {
		// this is from the CheckpointedRestoring interface.
		this.bufferedElements.addAll(state);
	}
}