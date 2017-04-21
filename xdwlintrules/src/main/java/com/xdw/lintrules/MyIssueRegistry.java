package com.xdw.lintrules;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xudengwang on 2017/4/21.
 */

public class MyIssueRegistry extends IssueRegistry {
    @Override
    public List<Issue> getIssues() {
        System.out.println("!!!!!!!!!!!!! xdw MyIssueRegistry lint rules works");
        return Arrays.asList(LoggerUsageDetector.ISSUE);
    }
}