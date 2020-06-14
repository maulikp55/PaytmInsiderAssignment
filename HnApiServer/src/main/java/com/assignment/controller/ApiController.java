package com.assignment.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.common.AppConstants;
import com.assignment.exceptions.NoRecordFoundException;
import com.assignment.model.Story;
import com.assignment.model.TopComment;
import com.assignment.service.ApiService;

@RestController
public class ApiController {

	private final Logger logger = LoggerFactory.getLogger(ApiController.class);

	@Autowired
	private ApiService hnApiService;

	/**
	 * Get the top 10 stories ranked by score in the last 10 minutes.
	 * 
	 * @return List<Story>
	 * 
	 */
	@GetMapping("/top-stories")
	public List<Story> getTopStories() {
		logger.info("Getting top 10 stories");
		List<Story> topStories = hnApiService.getTopStories();
		if (topStories.isEmpty()) {
			throw new NoRecordFoundException(AppConstants.NO_STORY_FOUND_MESSAGE);
		}

		return topStories;
	}

	/**
	 * Get Top 10 parent comments on a given story A story has several comments and
	 * each comment has child comments. Return only the parent comments sorted by
	 * total number of comments
	 * 
	 * @param id
	 * @return List<TopComment>
	 */
	@GetMapping("/comments/{storyId}")
	public List<TopComment> getTopComments(@PathVariable int storyId) {
		logger.info("Getting top 10 comments for Story with id: {}", storyId);
		/**
		 * Create a stream of SortedSet retrieved from getCommentsById in HnApiService
		 * and collect the top 10 comments into a list
		 */
		List<TopComment> topComments = hnApiService.getCommentsById(storyId).stream().limit(10)
				.collect(Collectors.toList());

		if (topComments.isEmpty()) {
			throw new NoRecordFoundException(AppConstants.NO_COMMENT_FOUND_MESSAGE);
		}

		return topComments;
	}

	/**
	 * Get past top stories served previously
	 * 
	 * @return Set<Story>
	 */
	@GetMapping("/past-stories")
	public Set<Story> getPastStories() {
		logger.info("Getting past served stories");
		Set<Story> pastStories = hnApiService.getPastTopStories();
		if (pastStories.isEmpty()) {
			throw new NoRecordFoundException(AppConstants.NO_STORY_FOUND_MESSAGE);
		}
		return pastStories;
	}
}
