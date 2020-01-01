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
package org.ml4j.nn.components.manytomany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ml4j.nn.components.ChainableDirectedComponent;
import org.ml4j.nn.components.ChainableDirectedComponentActivation;
import org.ml4j.nn.components.DirectedComponentsContext;
import org.ml4j.nn.components.manytomany.base.DefaultComponentChainBatchBase;
import org.ml4j.nn.components.onetone.DefaultDirectedComponentChain;
import org.ml4j.nn.components.onetone.DefaultDirectedComponentChainActivation;
import org.ml4j.nn.neurons.NeuronsActivation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultComponentChainBatchImpl extends DefaultComponentChainBatchBase implements DefaultDirectedComponentChainBatch {

	/**
	 * Default serialization id.
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultComponentChainBatchImpl.class);

	
	public DefaultComponentChainBatchImpl(List<DefaultDirectedComponentChain> parallelComponents) {
		super(parallelComponents);
	}
	
	@Override
	public DefaultDirectedComponentChainBatchActivation forwardPropagate(
			List<NeuronsActivation> neuronActivations, DirectedComponentsContext context) {
		int index = 0;
		List<DefaultDirectedComponentChainActivation> chainActivations = new ArrayList<>();
		for (NeuronsActivation neuronActivation : neuronActivations) {
			DefaultDirectedComponentChain chain = parallelComponents.get(index);
			DefaultDirectedComponentChainActivation chainActivation = chain.forwardPropagate(neuronActivation, context);
			chainActivations.add(chainActivation);
			index++;
		}
		LOGGER.debug("Forward propagating through DefaultComponentChainBatchImpl");

		return new DefaultDirectedComponentChainBatchActivationImpl(chainActivations);
	}
	
	@Override
	public DefaultDirectedComponentChainBatch dup() {
		return new DefaultComponentChainBatchImpl(parallelComponents);
	}

	@Override
	public DirectedComponentsContext getContext(DirectedComponentsContext directedComponentsContext,
			int componentIndex) {
		return directedComponentsContext;
	}

	@Override
	public List<ChainableDirectedComponent<List<NeuronsActivation>, ? extends ChainableDirectedComponentActivation<List<NeuronsActivation>>, ?>> decompose() {
		return Arrays.asList(this);
	}

}