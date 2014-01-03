package shiver.me.timbers.transform.language.test;

@SuppressWarnings("UnusedDeclaration")
public interface TransformerTestTemplate {

    public void testCreate();

    public void testCreateWithParentTransformations();

    public void testCreateWithNullParentTransformations();

    public void testTransform();

    public void testTransformKeywordsOnly();

    public void testTransformCommentsOnly();

    public void testTransformWithInvalidSource();

    public void testTransformWithTypesOnly();

    public void testTransformWithRulesOnly();

    public void testTransformWithNoTransformations();

    public void testTransformWithIrrelevantTransformations();

    public void testTransformWithNullInput();

    public void testTransformWithNullTransformations();
}
