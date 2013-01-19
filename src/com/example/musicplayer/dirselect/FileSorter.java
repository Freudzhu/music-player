package com.example.musicplayer.dirselect;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;

/**
 * Compares Files and sort it like:
 * 1.) Dir (alphabetical order)
 * 2.) Files (alphabetical order)
 */
public class FileSorter implements Comparator<File>
{
	public int compare(File lhs,
							 File rhs)
	{
		if (lhs == null)
		{
			return -1;
		}
		if (rhs == null)
		{
			return 1;
		}

		if (lhs.isDirectory())
		{
			if (rhs.isDirectory())
			{
				return nameCompare(lhs, rhs);
			} else
			{
				return -1;
			}
		} else
		{
			if (rhs.isDirectory())
			{
				return 1;
			} else
			{
				return nameCompare(lhs, rhs);
			}
		}
	}

	private int nameCompare(File lhs,
									File rhs)
	{
		return Collator.getInstance().compare(lhs.getAbsolutePath(), rhs.getAbsolutePath());
	}

}
