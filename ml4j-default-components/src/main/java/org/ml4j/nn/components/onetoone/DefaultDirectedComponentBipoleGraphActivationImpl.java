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
package org.ml4j.nn.components.onetoone;

import java.util.Arrays;
import java.util.List;

import org.ml4j.nn.components.DirectedComponentGradient;
import org.ml4j.nn.components.DirectedComponentGradientImpl;
import org.ml4j.nn.components.manytomany.DefaultDirectedComponentChainBatchActivation;
import org.ml4j.nn.components.manytoone.ManyToOneDirectedComponentActivation;
import org.ml4j.nn.components.onetomany.OneToManyDirectedComponentActivation;
import org.ml4j.nn.components.onetone.DefaultChainableDirectedComponentActivation;
import org.ml4j.nn.components.onetone.DefaultDirectedComponentBipoleGraph;
import org.ml4j.nn.components.onetone.DefaultDirectedComponentBipoleGraphActivation;
import org.ml4j.nn.components.onetoone.base.DefaultDirectedComponentBipoleGraphActivationBase;
import org.ml4j.nn.neurons.NeuronsActivation;

public class DefaultDirectedComponentBipoleGraphActivationImpl extends DefaultDirectedComponentBipoleGraphActivationBase
		implements DefaultDirectedComponentBipoleGraphActivation {
	
	private OneToManyDirectedComponentActivation oneToManyDirectedComponentActivation;
	private DefaultDirectedComponentChainBatchActivation parallelChainsActivation;
	private ManyToOneDirectedComponentActivation manyToOneDirectedComponentActivation;
	
	public DefaultDirectedComponentBipoleGraphActivationImpl(
			DefaultDirectedComponentBipoleGraph defaultDirectedComponentChainBipoleGraph,
			DefaultDirectedComponentChainBatchActivation parallelChainsActivation, 
			NeuronsActivation output) {
		super(defaultDirectedComponentChainBipoleGraph, output);
		this.parallelChainsActivation = parallelChainsActivation;
	}
	

	public DefaultDirectedComponentBipoleGraphActivationImpl(
			DefaultDirectedComponentBipoleGraph defaultDirectedComponentChainBipoleGraph,
			OneToManyDirectedComponentActivation oneToManyDirectedComponentActivation,
			DefaultDirectedComponentChainBatchActivation parallelChainsActivation,
			ManyToOneDirectedComponentActivation manyToOneDirectedComponentActivation) {
		super(defaultDirectedComponentChainBipoleGraph, manyToOneDirectedComponentActivation.getOutput());
		this.oneToManyDirectedComponentActivation = oneToManyDirectedComponentActivation;
		this.manyToOneDirectedComponentActivation = manyToOneDirectedComponentActivation;
		this.parallelChainsActivation = parallelChainsActivation;
	}

	@Override
	public DirectedComponentGradient<NeuronsActivation> backPropagate(
			DirectedComponentGradient<NeuronsActivation> outerGradient) {
		
		if (parallelChainsActivation.getActivations().size() == 1) {
			
			DirectedComponentGradient<List<NeuronsActivation>> gradient = new DirectedComponentGradientImpl<>(outerGradient.getTotalTrainableAxonsGradients(), Arrays.asList(outerGradient.getOutput()));
			DirectedComponentGradient<List<NeuronsActivation>> edgesGradients = parallelChainsActivation.backPropagate(gradient);
			return new DirectedComponentGradientImpl<>(edgesGradients.getTotalTrainableAxonsGradients(), edgesGradients.getOutput().get(0));
			
		} else {
		
			DirectedComponentGradient<List<NeuronsActivation>> manyToOneActivation = manyToOneDirectedComponentActivation.backPropagate(outerGradient);
			DirectedComponentGradient<List<NeuronsActivation>> edgesGradients = parallelChainsActivation.backPropagate(manyToOneActivation);
			return oneToManyDirectedComponentActivation.backPropagate(edgesGradients);
		
		}
	}

	@Override
	public List<DefaultChainableDirectedComponentActivation> decompose() {
		return Arrays.asList(this);
	}

}
