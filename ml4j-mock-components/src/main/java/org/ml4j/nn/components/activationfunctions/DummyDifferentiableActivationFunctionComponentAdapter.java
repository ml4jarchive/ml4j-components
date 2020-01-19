/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.ml4j.nn.components.activationfunctions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.ml4j.nn.activationfunctions.DifferentiableActivationFunction;
import org.ml4j.nn.components.activationfunctions.base.DifferentiableActivationFunctionComponentAdapterBase;
import org.ml4j.nn.neurons.Neurons;
import org.ml4j.nn.neurons.NeuronsActivation;
import org.ml4j.nn.neurons.NeuronsActivationContext;
import org.ml4j.nn.neurons.NeuronsActivationFeatureOrientation;

/**
 * Mock implementation of DifferentiableActivationFunctionComponent
 * 
 * @author Michael Lavelle
 *
 */
public class DummyDifferentiableActivationFunctionComponentAdapter extends DifferentiableActivationFunctionComponentAdapterBase implements DifferentiableActivationFunctionComponent {

	/**
	 * Generated serialization id.
	 */
	private static final long serialVersionUID = -6033017517698579773L;
	
	public DummyDifferentiableActivationFunctionComponentAdapter(Neurons neurons, DifferentiableActivationFunction activationFunction){
		super(neurons, activationFunction);
	}

	@Override
	public DifferentiableActivationFunctionComponentActivation forwardPropagate(NeuronsActivation inputActivation,
			NeuronsActivationContext context) {
		// Just return the input activation for this mock
		return new DummyDifferentiableActivationFunctionComponentActivation(this, inputActivation, inputActivation);
	}

	@Override
	public DifferentiableActivationFunctionComponentAdapter dup() {
		return new DummyDifferentiableActivationFunctionComponentAdapter(neurons, activationFunction);
	}

	@Override
	public Optional<NeuronsActivationFeatureOrientation> optimisedFor() {
		return Optional.empty();
	}

	@Override
	public List<NeuronsActivationFeatureOrientation> supports() {
		return Arrays.asList(NeuronsActivationFeatureOrientation.values());
	}

}