package test.utl;

import org.crumbs.core.util.ReflectionUtils;
import org.junit.Assert;
import org.junit.Test;
import test.utl.testclasses.*;

public class ReflectionUtilsTest {

    @Test
    public void shouldReturnTrueForTargetClass1() {
        boolean res = ReflectionUtils.hasAnnotation(TargetClass.class, TargetAnn.class);
        Assert.assertTrue(res);
    }

    @Test
    public void shouldReturnTrueForTargeClass2() {
        boolean res = ReflectionUtils.hasAnnotation(TargetClass2.class, TargetAnn.class);
        Assert.assertTrue(res);
    }

    @Test
    public void shouldReturnTrueForTargeClass3() {
        boolean res = ReflectionUtils.hasAnnotation(TargetClass3.class, TargetAnn.class);
        Assert.assertTrue(res);
    }

    @Test
    public void shouldReturnTrueForTargeClass4() {
        boolean res = ReflectionUtils.hasAnnotation(TargetClass4.class, TargetAnn.class);
        Assert.assertTrue(res);
    }

    @Test
    public void shouldReturnFalseForTargeClass5() {
        boolean res = ReflectionUtils.hasAnnotation(TargetClass5.class, TargetAnn.class);
        Assert.assertFalse(res);
    }
}
