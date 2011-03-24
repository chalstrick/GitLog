package com.example.org;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GitLog extends Activity {
	private static final String REPO_URI = "https://github.com/venkateshswdev/git_test.git";
	StringBuilder sb;
	TextView tv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sb = new StringBuilder();
		tv = new TextView(this);

		try {
			log("Trying create temporary directories");
			File f = File.createTempFile("oma", "tmp");
			String path = f.getAbsolutePath();
			f.delete();

			File p = new File(path);
			if (!p.mkdirs())
				log("Failed to create directory " + path);
			else
				log("Succeeded creating directory " + path);
			log("Trying to clone git repo:" + REPO_URI);
			Git git = Git.cloneRepository().setURI(REPO_URI).setDirectory(p)
					.call();
			log("Cloned succesfully!");
			log("Print shortlog");
			for (RevCommit c : git.log().call()) {
				log(c.getId() + "/" + c.getAuthorIdent().getName() + "/"
						+ c.getShortMessage());
			}

		} catch (Exception e) {
			log("got an exception: " + e);
		}
	}

	private void log(String s) {
		sb.append(s);
		sb.append('\n');
		tv.setText(sb);
		setContentView(tv);
	}
}