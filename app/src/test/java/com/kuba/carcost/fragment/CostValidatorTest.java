package com.kuba.carcost.fragment;

import com.kuba.carcost.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CostValidatorTest {

    @Mock
    private CostView view;
    @Mock
    private CostValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new CostValidator(view);
    }

    @Test
    public void shouldShowErrorMessageWhenMileageIsEmpty() throws Exception {
        when(view.getMileage()).thenReturn("");
        validator.validate();
        verify(view).showMileageError(R.string.empty_view_error);
    }

    @Test
    public void shouldShowErrorMessageWhenMileageIsNegative() throws Exception {
        when(view.getMileage()).thenReturn("-12345");
        validator.validate();
        verify(view).showMileageError(R.string.negative_value_error);
    }

    @Test
    public void shouldShowErrorMessageWhenExpenseIsEmpty() throws Exception {
        when(view.getMileage()).thenReturn("12345");
        when(view.getExpense()).thenReturn("");
        validator.validate();
        verify(view).showExpenseError(R.string.empty_view_error);
    }

    @Test
    public void shouldShowErrorMessageWhenExpenseIsNegative() throws Exception {
        when(view.getMileage()).thenReturn("1234");
        when(view.getExpense()).thenReturn("-123.4");
        validator.validate();
        verify(view).showMileageError(R.string.negative_value_error);
    }
}