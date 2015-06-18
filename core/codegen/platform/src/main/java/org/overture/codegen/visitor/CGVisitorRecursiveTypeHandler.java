package org.overture.codegen.visitor;

import java.util.Stack;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.node.INode;
import org.overture.ast.types.PType;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.types.AObjectTypeCG;
import org.overture.codegen.ir.IRInfo;

public class CGVisitorRecursiveTypeHandler extends CGVisitor<STypeCG> {

	private Stack<PType> typeStack;

	public CGVisitorRecursiveTypeHandler(
			AbstractVisitorCG<IRInfo, STypeCG> visitor) {
		super(visitor);

		this.typeStack = new Stack<PType>();
	}

	@Override
	public STypeCG defaultINode(INode node, IRInfo question)
			throws AnalysisException {

		if (node instanceof PType) {

			if (typeStack.contains(node)) {
				return new AObjectTypeCG();
			}

			typeStack.push((PType) node);

			STypeCG result = super.defaultINode(node, question);

			typeStack.pop();

			return result;
		}

		return super.defaultINode(node, question);
	}
}
