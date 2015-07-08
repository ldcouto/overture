package org.overture.interpreter.runtime;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.overture.ast.analysis.AnalysisAdaptor;
import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.expressions.AAndBooleanBinaryExp;
import org.overture.ast.expressions.AExists1Exp;
import org.overture.ast.expressions.AExistsExp;
import org.overture.ast.expressions.AForAllExp;
import org.overture.ast.expressions.AIfExp;
import org.overture.ast.expressions.ANotUnaryExp;
import org.overture.ast.expressions.AOrBooleanBinaryExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.expressions.SBooleanBinaryExp;
import org.overture.ast.intf.lex.ILexLocation;
import org.overture.ast.statements.AElseIfStm;
import org.overture.ast.statements.AIfStm;
import org.overture.ast.statements.AWhileStm;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

//Analysis Adaptor
public class GenerateTestCases extends AnalysisAdaptor {
	private Document doc;
	private Element rootElement;
	private Element currentElement;
	public HashMap<ILexLocation, Element> xml_nodes;

	public GenerateTestCases(String file_name) {
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		this.doc = db.newDocument();
		this.rootElement = doc.createElement("file");
		Element fname = doc.createElement("file_name");
		fname.setTextContent(file_name);
		rootElement.appendChild(fname);
		this.currentElement = rootElement;
		this.doc.appendChild(rootElement);
		this.xml_nodes = new HashMap<>();
	}

	public static void fill_source_file_location(Element and, ILexLocation local) {
		and.setAttribute("start_line", Integer.toString(local.getStartLine()));
		and.setAttribute("start_column", Integer.toString(local.getStartPos()));
		and.setAttribute("end_line", Integer.toString(local.getEndLine()));
		and.setAttribute("end_column", Integer.toString(local.getEndPos()));
	}

	public void saveCoverageXml(File coverage, String filename) {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(coverage.getPath()
				+ File.separator + filename + "test_cases.xml"));
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void caseANotUnaryExp(ANotUnaryExp node) throws AnalysisException {
		PExp expression = node.getExp();
		expression.apply(this);
	}

	@Override
	public void caseAIfStm(AIfStm node) throws AnalysisException {
		ILexLocation local = node.getLocation();
		PExp exp = node.getIfExp();
		Element if_statement = doc.createElement("if_statement");
		Element source_code = doc.createElement("source_code");
		source_code.setTextContent(node.getIfExp().toString());
		if_statement.appendChild(source_code);
		fill_source_file_location(if_statement, local);
		currentElement = if_statement;
		Element condition = doc.createElement("condition");
		condition.appendChild(source_code.cloneNode(true));
		fill_source_file_location(condition, exp.getLocation());
		Element eval_true = doc.createElement("evaluation");
		eval_true.setAttribute("tested", "false");
		eval_true.setAttribute("n", "1");
		eval_true.setTextContent("true");
		condition.appendChild(eval_true);

		Element eval_false = doc.createElement("evaluation");
		eval_false.setAttribute("tested", "false");
		eval_false.setAttribute("n", "2");
		eval_false.setTextContent("false");
		condition.appendChild(eval_false);

		Element outcome = (Element) eval_true.cloneNode(true);
		Element outcome2 = (Element) eval_false.cloneNode(true);

		if_statement.appendChild(outcome);
		if_statement.appendChild(outcome2);

		currentElement.appendChild(condition);
		rootElement.appendChild(if_statement);
		xml_nodes.put(local, if_statement);
		xml_nodes.put(node.getIfExp().getLocation(), condition);
		exp.apply(this);
	}

	@Override
	public void caseAExists1Exp(AExists1Exp node) throws AnalysisException {
		Element condition = (Element) xml_nodes.get(node.getLocation())
				.cloneNode(true);
		fill_source_file_location(condition, node.getPredicate().getLocation());
		currentElement.replaceChild(condition,
				xml_nodes.get(node.getLocation()));
		xml_nodes.remove(node.getLocation());
		xml_nodes.put(node.getLocation(), condition);
		node.getPredicate().apply(this);
	}

	@Override
	public void caseAExistsExp(AExistsExp node) throws AnalysisException {
		Element condition = (Element) xml_nodes.get(node.getLocation())
				.cloneNode(true);
		fill_source_file_location(condition, node.getPredicate().getLocation());
		currentElement.replaceChild(condition,
				xml_nodes.get(node.getLocation()));
		xml_nodes.remove(node.getLocation());
		xml_nodes.put(node.getLocation(), condition);
		node.getPredicate().apply(this);
	}

	@Override
	public void caseAElseIfStm(AElseIfStm node) throws AnalysisException {
		ILexLocation local = node.getLocation();
		PExp exp = node.getElseIf();

		Element elseif_statement = doc.createElement("elseif_statement");
		Element source_code = doc.createElement("source_code");
		source_code.setTextContent(node.getElseIf().toString());
		elseif_statement.appendChild(source_code);
		fill_source_file_location(elseif_statement, local);
		currentElement = elseif_statement;
		Element condition = doc.createElement("condition");
		condition.appendChild(source_code.cloneNode(true));
		fill_source_file_location(condition, exp.getLocation());
		Element eval_true = doc.createElement("evaluation");
		eval_true.setAttribute("tested", "false");
		eval_true.setAttribute("n", "1");
		eval_true.setTextContent("true");
		condition.appendChild(eval_true);

		Element eval_false = doc.createElement("evaluation");
		eval_false.setAttribute("tested", "false");
		eval_false.setAttribute("n", "2");
		eval_false.setTextContent("false");
		condition.appendChild(eval_false);

		Element outcome = (Element) eval_true.cloneNode(true);
		Element outcome2 = (Element) eval_false.cloneNode(true);

		elseif_statement.appendChild(outcome);
		elseif_statement.appendChild(outcome2);

		currentElement.appendChild(condition);
		rootElement.appendChild(elseif_statement);
		xml_nodes.put(local, elseif_statement);
		xml_nodes.put(node.getElseIf().getLocation(), condition);
		exp.apply(this);
	}

	@Override
	public void caseAForAllExp(AForAllExp node) throws AnalysisException {
		Element condition = (Element) xml_nodes.get(node.getLocation())
				.cloneNode(true);
		fill_source_file_location(condition, node.getPredicate().getLocation());
		currentElement.replaceChild(condition,
				xml_nodes.get(node.getLocation()));
		xml_nodes.remove(node.getLocation());
		xml_nodes.put(node.getLocation(), condition);
		node.getPredicate().apply(this);
	}

	@Override
	public void caseAWhileStm(AWhileStm node) throws AnalysisException {
		ILexLocation local = node.getLocation();
		PExp exp = node.getExp();

		Element while_statement = doc.createElement("while_statement");
		Element source_code = doc.createElement("source_code");
		source_code.setTextContent(node.getExp().toString());
		while_statement.appendChild(source_code);
		fill_source_file_location(while_statement, local);
		currentElement = while_statement;
		Element condition = doc.createElement("condition");
		condition.appendChild(source_code.cloneNode(true));
		fill_source_file_location(condition, exp.getLocation());
		Element eval_true = doc.createElement("evaluation");
		eval_true.setAttribute("tested", "false");
		eval_true.setAttribute("n", "1");
		eval_true.setTextContent("true");
		condition.appendChild(eval_true);

		Element eval_false = doc.createElement("evaluation");
		eval_false.setAttribute("tested", "false");
		eval_false.setAttribute("n", "2");
		eval_false.setTextContent("false");
		condition.appendChild(eval_false);

		Element outcome = (Element) eval_true.cloneNode(true);
		Element outcome2 = (Element) eval_false.cloneNode(true);

		while_statement.appendChild(outcome);
		while_statement.appendChild(outcome2);

		currentElement.appendChild(condition);
		rootElement.appendChild(while_statement);
		xml_nodes.put(local, while_statement);
		xml_nodes.put(node.getExp().getLocation(), condition);
		exp.apply(this);

	}

	@Override
	public void caseAAndBooleanBinaryExp(AAndBooleanBinaryExp node)
			throws AnalysisException {
		Element previous_condition = (Element) xml_nodes
				.get(node.getLocation()).cloneNode(true);
		NodeList previous_evaluations = previous_condition
				.getElementsByTagName("evaluation");

		Element new_condition_left = doc.createElement("condition");
		Element source_code_left = doc.createElement("source_code");
		source_code_left.setTextContent(node.getLeft().toString());
		new_condition_left.appendChild(source_code_left);

		Element new_condition_right = doc.createElement("condition");
		Element source_code_right = doc.createElement("source_code");
		source_code_right.setTextContent(node.getRight().toString());
		new_condition_right.appendChild(source_code_right);

		fill_source_file_location(new_condition_left, node.getLeft()
				.getLocation());
		fill_source_file_location(new_condition_right, node.getRight()
				.getLocation());

		xml_nodes.get(node.getLocation()).getParentNode()
				.removeChild(xml_nodes.get(node.getLocation()));
		xml_nodes.remove(node.getLocation());

		for (int i = 0; i < previous_evaluations.getLength(); i++) {
			Element evaluation = (Element) previous_evaluations.item(i);
			String content = evaluation.getTextContent();
			String test_number = evaluation.getAttribute("n");

			if (content.equals("?")) {
				Element new_eval_left = doc.createElement("evaluation");
				new_eval_left.setAttribute("tested", "false");
				new_eval_left.setAttribute("n", test_number);
				new_eval_left.setTextContent("?");
				Element new_eval_right = (Element) new_eval_left
						.cloneNode(true);

				new_condition_left.appendChild(new_eval_left);
				new_condition_right.appendChild(new_eval_right);
			} else if (content.equals("true")) {
				Element new_eval_left = doc.createElement("evaluation");
				new_eval_left.setAttribute("tested", "false");
				new_eval_left.setAttribute("n", test_number);
				new_eval_left.setTextContent("true");
				Element new_eval_right = (Element) new_eval_left
						.cloneNode(true);

				new_condition_left.appendChild(new_eval_left);
				new_condition_right.appendChild(new_eval_right);
			} else {
				Element new_eval_left = doc.createElement("evaluation");
				new_eval_left.setAttribute("tested", "false");
				new_eval_left.setAttribute("n", test_number);
				new_eval_left.setTextContent("false");

				Element new_eval_right = doc.createElement("evaluation");
				new_eval_right.setAttribute("tested", "false");
				new_eval_right.setAttribute("n", test_number);
				new_eval_right.setTextContent("true");

				Element new_eval_left1 = doc.createElement("evaluation");
				new_eval_left1.setAttribute("tested", "false");
				new_eval_left1.setAttribute("n",
						String.valueOf(1 + previous_evaluations.getLength()));
				new_eval_left1.setTextContent("false");

				Element new_eval_right1 = doc.createElement("evaluation");
				new_eval_right1.setAttribute("tested", "false");
				new_eval_right1.setAttribute("n",
						String.valueOf(1 + previous_evaluations.getLength()));
				new_eval_right1.setTextContent("?");

				currentElement.appendChild(new_eval_left1.cloneNode(true));

				duplicate_evaluation(test_number);

				if (node.getRight() instanceof SBooleanBinaryExp) {
					new_condition_left.appendChild(new_eval_left);
					new_condition_right.appendChild(new_eval_right);
					new_condition_left.appendChild(new_eval_right1);
					new_condition_right.appendChild(new_eval_left1);
				} else {
					new_condition_left.appendChild(new_eval_right);
					new_condition_right.appendChild(new_eval_left);
					new_condition_left.appendChild(new_eval_left1);
					new_condition_right.appendChild(new_eval_right1);
				}

			}
		}
		currentElement.appendChild(new_condition_right);
		currentElement.appendChild(new_condition_left);
		xml_nodes.put(node.getLeft().getLocation(), new_condition_left);
		xml_nodes.put(node.getRight().getLocation(), new_condition_right);

		node.getLeft().apply(this);
		node.getRight().apply(this);
	}

	public void duplicate_evaluation(String evaluation_number) {
		NodeList conditions = currentElement.getElementsByTagName("condition");
		for (int i = 0; i < conditions.getLength(); i++) {
			Element condition = (Element) conditions.item(i);
			NodeList evaluations = condition.getElementsByTagName("evaluation");
			for (int j = 0; j < evaluations.getLength(); j++) {
				Element evaluation = (Element) evaluations.item(j);
				if (evaluation.getAttribute("n").equals(evaluation_number)) {
					Element new_evaluation = (Element) evaluation
							.cloneNode(true);
					new_evaluation.setAttribute("n",
							String.valueOf(1 + evaluations.getLength()));
					conditions.item(i).appendChild(new_evaluation);
				}
			}
		}
	}

	@Override
	public void caseAOrBooleanBinaryExp(AOrBooleanBinaryExp node)
			throws AnalysisException {
		Element previous_condition = (Element) xml_nodes
				.get(node.getLocation()).cloneNode(true);
		NodeList previous_evaluations = previous_condition
				.getElementsByTagName("evaluation");

		Element new_condition_left = doc.createElement("condition");
		Element source_code_left = doc.createElement("source_code");
		source_code_left.setTextContent(node.getLeft().toString());
		new_condition_left.appendChild(source_code_left);

		Element new_condition_right = doc.createElement("condition");
		Element source_code_right = doc.createElement("source_code");
		source_code_right.setTextContent(node.getRight().toString());
		new_condition_right.appendChild(source_code_right);

		fill_source_file_location(new_condition_left, node.getLeft()
				.getLocation());
		fill_source_file_location(new_condition_right, node.getRight()
				.getLocation());

		xml_nodes.get(node.getLocation()).getParentNode()
				.removeChild(xml_nodes.get(node.getLocation()));
		xml_nodes.remove(node.getLocation());

		for (int i = 0; i < previous_evaluations.getLength(); i++) {
			Element evaluation = (Element) previous_evaluations.item(i);
			String content = evaluation.getTextContent();
			String test_number = evaluation.getAttribute("n");

			if (content.equals("?")) {
				Element new_eval_left = doc.createElement("evaluation");
				new_eval_left.setAttribute("n", test_number);
				new_eval_left.setAttribute("tested", "false");
				new_eval_left.setTextContent("?");
				Element new_eval_right = (Element) new_eval_left
						.cloneNode(true);

				new_condition_left.appendChild(new_eval_left);
				new_condition_right.appendChild(new_eval_right);
			} else if (content.equals("false")) {
				Element new_eval_left = doc.createElement("evaluation");
				new_eval_left.setAttribute("n", test_number);
				new_eval_left.setAttribute("tested", "false");
				new_eval_left.setTextContent("false");
				Element new_eval_right = (Element) new_eval_left
						.cloneNode(true);

				new_condition_left.appendChild(new_eval_left);
				new_condition_right.appendChild(new_eval_right);
			} else {
				Element new_eval_left = doc.createElement("evaluation");
				new_eval_left.setAttribute("n", test_number);
				new_eval_left.setAttribute("tested", "false");
				new_eval_left.setTextContent("false");

				Element new_eval_right = doc.createElement("evaluation");
				new_eval_right.setAttribute("n", test_number);
				new_eval_right.setAttribute("tested", "false");
				new_eval_right.setTextContent("true");

				Element new_eval_left1 = doc.createElement("evaluation");
				new_eval_left1.setAttribute(
						"n",
						String.valueOf(Integer.valueOf(test_number)
								+ previous_evaluations.getLength()));
				new_eval_left1.setAttribute("tested", "false");
				new_eval_left1.setTextContent("true");

				Element new_eval_right1 = doc.createElement("evaluation");
				new_eval_right1.setAttribute(
						"n",
						String.valueOf(Integer.valueOf(test_number)
								+ previous_evaluations.getLength()));
				new_eval_right1.setAttribute("tested", "false");
				new_eval_right1.setTextContent("?");

				duplicate_evaluation(test_number);

				currentElement.appendChild(new_eval_left1.cloneNode(true));

				if (node.getRight() instanceof SBooleanBinaryExp) {
					new_condition_left.appendChild(new_eval_left);
					new_condition_right.appendChild(new_eval_right);
					new_condition_left.appendChild(new_eval_left1);
					new_condition_right.appendChild(new_eval_right1);
				} else {
					new_condition_left.appendChild(new_eval_right);
					new_condition_right.appendChild(new_eval_left);
					new_condition_left.appendChild(new_eval_right1);
					new_condition_right.appendChild(new_eval_left1);
				}

			}
		}
		currentElement.appendChild(new_condition_right);
		currentElement.appendChild(new_condition_left);
		xml_nodes.put(node.getLeft().getLocation(), new_condition_left);
		xml_nodes.put(node.getRight().getLocation(), new_condition_right);

		node.getLeft().apply(this);
		node.getRight().apply(this);

	}

	@Override
	public void caseAIfExp(AIfExp node) throws AnalysisException {
		// TODO Auto-generated method stub
		ILexLocation local = node.getLocation();
		PExp exp = node.getTest();

		Element if_expression = doc.createElement("if_expression");
		fill_source_file_location(if_expression, local);
		currentElement = if_expression;
		Element source_code = doc.createElement("source_code");
		source_code.setTextContent(node.getTest().toString());
		if_expression.appendChild(source_code);
		Element condition = doc.createElement("condition");
		condition.appendChild(source_code.cloneNode(true));
		fill_source_file_location(condition, exp.getLocation());
		Element eval_true = doc.createElement("evaluation");
		eval_true.setAttribute("tested", "false");
		eval_true.setAttribute("n", "1");
		eval_true.setTextContent("true");
		condition.appendChild(eval_true);

		Element eval_false = doc.createElement("evaluation");
		eval_false.setAttribute("tested", "false");
		eval_false.setAttribute("n", "2");
		eval_false.setTextContent("false");
		condition.appendChild(eval_false);

		Element outcome = (Element) eval_true.cloneNode(true);
		Element outcome2 = (Element) eval_false.cloneNode(true);

		if_expression.appendChild(outcome);
		if_expression.appendChild(outcome2);

		currentElement.appendChild(condition);
		rootElement.appendChild(if_expression);
		xml_nodes.put(local, if_expression);
		xml_nodes.put(node.getTest().getLocation(), condition);
		exp.apply(this);
	}

	public float getTestedRate() {
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList tested =null, not_tested = null;
		try {
			not_tested = (NodeList) xPath
					.evaluate(
							"//evaluation[not(@n = ../preceding-sibling::evaluation/@n) and not(@n = ../following-sibling::evaluation/@n) and @tested='false']",
							doc.getDocumentElement(), XPathConstants.NODESET);

			tested = (NodeList) xPath
					.evaluate(
							"//evaluation[not(@n = ../preceding-sibling::evaluation/@n) and not(@n = ../following-sibling::evaluation/@n) and @tested='true']",
							doc.getDocumentElement(), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (float) ((float) tested.getLength() * 100.0 / (float) ((float)not_tested.getLength() + (float)tested.getLength()));
	}
}
