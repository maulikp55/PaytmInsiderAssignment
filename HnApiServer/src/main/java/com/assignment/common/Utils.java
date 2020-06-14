package com.assignment.common;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assignment.exceptions.ConnectionException;
import com.assignment.exceptions.InternalServerException;
import com.assignment.model.Comment;
import com.assignment.model.Story;
import com.assignment.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	private final static Logger logger = LoggerFactory.getLogger(Utils.class);

	private Utils() {
		throw new IllegalStateException("Utils class: Instantiation not allowed");
	}

	private static ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public static List<Integer> getTopStoryIds() {
		List<Integer> topStories;
		URL url;
		try {
			url = new URL(AppConstants.HACKER_NEWS_API_BASE_URL + AppConstants.TOP_STORIES_END_POINT
					+ AppConstants.ENDPOINT_SUFFIX);
			topStories = objectMapper.readValue(url, new TypeReference<List<Integer>>() {
			});
		} catch (UnknownHostException e) {
			logger.error(e.toString());
			throw new ConnectionException();
		} catch (Exception e) {
			logger.error(e.toString());
			throw new InternalServerException();
		}
		return topStories;
	}

	public static Story getStory(int storyId) {
		Story story = null;
		try {
			story = objectMapper.readValue(new URL(AppConstants.HACKER_NEWS_API_BASE_URL + AppConstants.ITEM_END_POINT
					+ storyId + AppConstants.ENDPOINT_SUFFIX), Story.class);
		} catch (UnknownHostException e) {
			logger.error(e.toString());
			throw new ConnectionException();
		} catch (Exception e) {
			logger.error(e.toString());
			throw new InternalServerException();
		}
		return story;
	}

	public static Comment getComment(int commentId) {
		Comment comment = null;
		try {
			comment = objectMapper.readValue(new URL(AppConstants.HACKER_NEWS_API_BASE_URL + AppConstants.ITEM_END_POINT
					+ commentId + AppConstants.ENDPOINT_SUFFIX), Comment.class);

		} catch (UnknownHostException e) {
			logger.error(e.toString());
			throw new ConnectionException();
		} catch (Exception e) {
			logger.error(e.toString());
			throw new InternalServerException();
		}
		return comment;
	}

	public static int getCommentCount(Comment comment) {
		if (comment == null) {
			return 0;
		}
		int count = 0;

		if (comment.getKids() != null) {

			count += comment.getKids().length;

			for (int commentId : comment.getKids()) {
				try {
					Comment kidComment = objectMapper.readValue(new URL(AppConstants.HACKER_NEWS_API_BASE_URL
							+ AppConstants.ITEM_END_POINT + commentId + AppConstants.ENDPOINT_SUFFIX), Comment.class);
					count += getCommentCount(kidComment);
				} catch (UnknownHostException e) {
					logger.error(e.toString());
					throw new ConnectionException();
				} catch (IOException e) {
					logger.error(e.toString());
					throw new InternalServerException();
				}

			}

		}
		return count;
	}

	/**
	 * Method to calculate HackerNews age of a user i.e. how old their Hacker News
	 * profile is in years.
	 * 
	 * @param name
	 * @return
	 */
	public static int getUserAgeByName(String name) {
		User user = null;

		try {
			user = objectMapper.readValue(new URL(AppConstants.HACKER_NEWS_API_BASE_URL + AppConstants.USER_END_POINT
					+ name + AppConstants.ENDPOINT_SUFFIX), User.class);
		} catch (IOException e) {
			logger.error("Error getting age for user: {}", name);
		}
		if (user != null) {

			Instant instantOfNow = Instant.now();
			LocalDate localDate = LocalDateTime.ofInstant(instantOfNow, ZoneId.systemDefault()).toLocalDate();

			return Period.between(localDate, LocalDate.now()).getYears();
		}
		return 0;
	}

}
