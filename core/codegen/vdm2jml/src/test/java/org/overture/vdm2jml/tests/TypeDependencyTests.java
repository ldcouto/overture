package org.overture.vdm2jml.tests;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.overture.ast.modules.AModuleModules;
import org.overture.ast.types.ACharBasicType;
import org.overture.ast.types.ANatNumericBasicType;
import org.overture.ast.util.modules.ModuleList;
import org.overture.codegen.utils.GeneralCodeGenUtils;
import org.overture.codegen.vdm2jml.LeafTypeInfo;
import org.overture.codegen.vdm2jml.NamedTypeInfo;
import org.overture.codegen.vdm2jml.NamedTypeInvDepCalculator;

public class TypeDependencyTests
{
	public static final String TEST_RES_TYPE_DEP_ROOT = AnnotationTestsBase.TEST_RESOURCES_ROOT
			+ "type_dep" + File.separatorChar;

	public static final String MODULE = "Entry";

	private List<NamedTypeInfo> typeInfoList;

	public void load(String filename)
	{
		try
		{
			List<File> files = new LinkedList<File>();

			files.add(new File(TEST_RES_TYPE_DEP_ROOT + filename));

			ModuleList modules = GeneralCodeGenUtils.consModuleList(files);

			Assert.assertTrue("Expected a single module but got "
					+ modules.size(), modules.size() == 1);

			AModuleModules module = modules.get(0);

			NamedTypeInvDepCalculator depCalc = new NamedTypeInvDepCalculator();

			module.apply(depCalc);

			this.typeInfoList = depCalc.getTypeDataList();
			Assert.assertTrue("Could not load type info", this.typeInfoList != null);

			for (NamedTypeInfo info : typeInfoList)
			{
				Assert.assertEquals("Expected the enclosing module to be '"
						+ MODULE + "'", MODULE, info.getDefModule());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			Assert.assertFalse("Problems loading test data: " + e.getMessage(), true);
		}
	}

	private void assertTypeName(String typeName, NamedTypeInfo info)
	{
		Assert.assertEquals("Expected type name to be '" + typeName + "'", typeName, info.getTypeName());
	}

	private void assertNamedChildren(NamedTypeInfo info, int no)
	{
		Assert.assertTrue("Expected " + no + " named types for type "
				+ infoStr(info), info.getNamedTypes().size() == no);
	}

	private String infoStr(NamedTypeInfo info)
	{
		String message = info.getDefModule() + "." + info.getTypeName();
		return message;
	}

	private void assertNoOfLeafs(NamedTypeInfo info, int no)
	{
		Assert.assertEquals("Number of actual leaf types differs from those expected", no, info.getLeafTypes().size());
	}

	private void assertLeafType(NamedTypeInfo info, Class<?> leafType,
			boolean nullAllowed)
	{
		for (LeafTypeInfo leaf : info.getLeafTypes())
		{
			if (leafType == leaf.getType().getClass())
			{
				Assert.assertEquals("Found leaf type but 'allowsNull' does not equal", nullAllowed, leaf.allowsNull());
				return;
			}
		}

		Assert.assertFalse("Could not find leaf type '" + leafType
				+ " with ' nullAllowed: '" + nullAllowed
				+ "' for named invariant type '" + infoStr(info) + "'", true);
	}

	private void assertNullAllowed(NamedTypeInfo info)
	{
		Assert.assertTrue("Expected named type invariant '" + infoStr(info)
				+ "' to allow null", info.allowsNull());
	}

	private void assertNullNotAllowed(NamedTypeInfo info)
	{
		Assert.assertTrue("Expected named type invariant '" + infoStr(info)
				+ "' NOT to allow null", !info.allowsNull());
	}

	private void assertTotalNoOfNamedInvTypes(int no)
	{
		Assert.assertTrue("Expected " + no + " named invariant types but got "
				+ typeInfoList.size(), typeInfoList.size() == no);
	}

	private void assertNoInv(NamedTypeInfo info)
	{
		Assert.assertTrue("Expected named invariant type '" + infoStr(info)
				+ "' to NOT have an invariant", !info.hasInv());
	}

	private void assertInv(NamedTypeInfo info)
	{
		Assert.assertTrue("Expected named invariant type '" + infoStr(info)
				+ "' to have an invariant", info.hasInv());
	}

	private NamedTypeInfo getInfo(String typeName)
	{
		NamedTypeInfo info = NamedTypeInvDepCalculator.findTypeInfo(typeInfoList, MODULE, typeName);
		assertTypeName(typeName, info);
		return info;
	}

	@Test
	public void namedInvTypeNat()
	{
		load("Nat.vdmsl");

		// N = nat;
		String typeName = "N";
		NamedTypeInfo info = getInfo(typeName);

		assertTotalNoOfNamedInvTypes(1);
		assertNamedChildren(info, 0);
		assertNoOfLeafs(info, 1);
		assertLeafType(info, ANatNumericBasicType.class, false);
		assertNullNotAllowed(info);
		assertNoInv(info);
	}

	@Test
	public void namedInvTypeNatOpt()
	{
		// N = [nat];
		load("NatOpt.vdmsl");

		String typeName = "N";
		NamedTypeInfo info = getInfo(typeName);

		assertTotalNoOfNamedInvTypes(1);
		assertNamedChildren(info, 0);
		assertNoOfLeafs(info, 1);
		assertLeafType(info, ANatNumericBasicType.class, true);
		assertNullAllowed(info);
		assertNoInv(info);
	}

	@Test
	public void unionOfBasicAndOptBasic()
	{
		// CN = nat|[char];
		load("UnionOfBasicAndOptBasic.vdmsl");

		String typeName = "CN";
		NamedTypeInfo info = getInfo(typeName);

		assertTotalNoOfNamedInvTypes(1);
		assertNamedChildren(info, 0);
		assertNoOfLeafs(info, 2);
		assertLeafType(info, ANatNumericBasicType.class, false);
		assertLeafType(info, ACharBasicType.class, true);
		assertNullAllowed(info);
		assertNoInv(info);
	}

	@Test
	public void unionOfNamedInvTypes()
	{
		// CN = C|N;
		// N = nat;
		// C = [char];

		load("UnionOfNamedInvTypes.vdmsl");

		String typeName = "CN";
		NamedTypeInfo info = getInfo(typeName);

		assertTotalNoOfNamedInvTypes(3);
		assertNamedChildren(info, 2);
		assertNoOfLeafs(info, 0);
		assertNullAllowed(info);
		assertNoInv(info);

		typeName = "N";
		info = getInfo(typeName);

		assertNamedChildren(info, 0);
		assertNoOfLeafs(info, 1);
		assertLeafType(info, ANatNumericBasicType.class, false);
		assertNullNotAllowed(info);
		assertNoInv(info);

		typeName = "C";
		info = getInfo(typeName);

		assertNamedChildren(info, 0);
		assertNoOfLeafs(info, 1);
		assertLeafType(info, ACharBasicType.class, true);
		assertNullAllowed(info);
		assertNoInv(info);
	}

	@Test
	public void invariants()
	{
		// CN = C|N
		// inv cn == is_char(cn) => cn = 'a';
		// N = nat
		// inv n == n = 1;
		// C = [char];
		load("Invariants.vdmsl");

		assertInv(getInfo("CN"));
		assertInv(getInfo("N"));
		assertNoInv(getInfo("C"));
	}

	@Test
	public void optionalUnion()
	{
		load("OptionalUnion.vdmsl");
		// CN = [C|N];
		// N = nat;
		// C = char;

		assertNullAllowed(getInfo("CN"));
		assertNullNotAllowed(getInfo("N"));
		assertNullNotAllowed(getInfo("C"));
	}

	@Test
	public void record()
	{
		load("Rec.vdmsl");
		// R :: x : int
		// inv r == r.x = 1;
		//
		// Rn = R | nat;

		String typeName = "Rn";
		NamedTypeInfo info = getInfo(typeName);

		// We do not expect the record to be included
		assertTotalNoOfNamedInvTypes(1);
		assertNamedChildren(info, 0);
		// We expect to have the record type 'R' registered as a leaf type
		assertNoOfLeafs(info, 2);
	}

	@Test
	public void recursive()
	{
		load("Recursive.vdmsl");
		// T = nat | T;

		String typeName = "T";

		NamedTypeInfo info = getInfo(typeName);

		assertTotalNoOfNamedInvTypes(1);
		assertNamedChildren(info, 0);
		assertNoOfLeafs(info, 1);
		assertLeafType(info, ANatNumericBasicType.class, false);
	}
	
	@Test
	public void unionWithoutNull()
	{
		load("UnionWithoutNull.vdmsl");
		//CN = C|N;
		//N = nat;
		//C = char;
		
		assertNullNotAllowed(getInfo("CN"));
	}
}
