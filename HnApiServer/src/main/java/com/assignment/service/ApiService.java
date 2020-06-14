package com.assignment.service;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import com.assignment.model.Story;
import com.assignment.model.TopComment;

public interface ApiService {
	/**
	 * Get the Top 10 stories
	 * 
	 * @return List<Story>
	 */
	List<Story> getTopStories();

	/**
	 * Get top parent comments for a provided storyId
	 * 
	 * @param storyId
	 * @return SortedSet<TopComment>
	 */
	SortedSet<TopComment> getCommentsById(int storyId);

	/**
	 * Get all the previously served top stories
	 * 
	 * @return Set<Story>
	 */
	Set<Story> getPastTopStories();
}
