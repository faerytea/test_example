package com.github.faerytea.cttdev.tests;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BrokenSortSelectionTest {
    @Mock
    public View fakeButton;
    @Mock
    public MainActivity mainActivity;

    @Before
    public void setUp() {
        when(fakeButton.getId()).thenReturn(-1);
        doCallRealMethod().when(mainActivity).onSortClick(any());
    }

    @Test
    public void nothingWillHappenOnUnknownView() {
        mainActivity.onSortClick(fakeButton);
    }
}
