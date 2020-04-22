package com.bridgelabz.fundoonotes.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.configuration.ElasticSearchConfigure;
import com.bridgelabz.fundoonotes.entity.Note;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ElasticRepositoryImpl implements ElasticSearchRepo{

	@Autowired
	private ElasticSearchConfigure config;

	@Autowired
	private ObjectMapper objectmapper;
	private static final String INDEX = "notes";
	private static final String TYPE = "note";

	@Override
	public void createNote(Note note) {
		IndexRequest indexrequest = new IndexRequest(INDEX, TYPE, String.valueOf(note.getNoteID()))
				.source(objectmapper.convertValue(note, Map.class));
		try {
			config.client().index(indexrequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String updateNote(Note note) {
		System.out.println(note.getNoteID());
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(note.getNoteID()))
				.doc(objectmapper.convertValue(note, Map.class));
		UpdateResponse updateResponse = null;
		try {
			updateResponse = config.client().update(updateRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return updateResponse.getResult().name();
	}

	@Override
	public String deleteNote(Note note) {

		objectmapper.convertValue(note, Map.class);
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, String.valueOf(note.getNoteID()));
		DeleteResponse deleteResponse = null;
		try {
			deleteResponse = config.client().delete(deleteRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return deleteResponse.getResult().name();
	}

	@Override
	public List<Note> searchByTitle(String title) {
		SearchRequest searchRequest = new SearchRequest(INDEX);
		SearchSourceBuilder searchSource = new SearchSourceBuilder(); // System.out.println(searchRequest);

		searchSource.query(QueryBuilders.matchQuery("title", title));
		searchRequest.source(searchSource);
		SearchResponse searchResponse = null;
		try {
			searchResponse = config.client().search(searchRequest, RequestOptions.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(getResult(searchResponse).toString());
		return getResult(searchResponse);
	}

	private List<Note> getResult(SearchResponse searchResponse) {
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		List<Note> notes = new ArrayList<>();
		if (searchHits.length > 0) {
			Arrays.stream(searchHits)
					.forEach(hit -> notes.add(objectmapper.convertValue(hit.getSourceAsMap(), Note.class)));
		}
		return notes;
	}

}
