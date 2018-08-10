package net.dertod2.SimpleUnlock.Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import net.dertod2.DatabaseHandler.Binary.DatabaseHandler;
import net.dertod2.DatabaseHandler.Table.TableEntry;

public class UnlockControl {
	private Map<Integer, Question> questionList;
	
	public UnlockControl() {
		try {
			DatabaseHandler.getHandler().updateLayout(new Question());
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public void load() {
		List<TableEntry> dataList = new ArrayList<TableEntry>();
		
		if (this.questionList == null) this.questionList = new HashMap<Integer, Question>();
		
		this.questionList.clear();
		
		try {
			DatabaseHandler.getHandler().load(new Question(), dataList);
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			for (TableEntry tableEntry : dataList) {
				Question question = (Question) tableEntry;
				this.questionList.put(question.getId(), question);
			}
		}
	}
	
	protected Question getQuestion(int questionId) {
		return this.questionList.get(questionId);
	}
	
	protected List<Question> getQuestions() {
		return ImmutableList.<Question>copyOf(this.questionList.values());
	}
	
	protected Question getRandom(List<Integer> askedQuestions) {
		if (this.questionList.size() <= askedQuestions.size()) return null;
		
		List<Question> unansweredList = new ArrayList<Question>();
		
		for (Question question : this.questionList.values()) {
			if (askedQuestions.contains(question.getId())) continue;
			unansweredList.add(question);
		}
		
		Random random = new Random(System.currentTimeMillis());
		int randomId = random.nextInt(unansweredList.size());
		
		return unansweredList.get(randomId);
	}
	
	protected void add(Question question) {
		this.questionList.put(question.getId(), question);
	}
	
	protected void delete(Question question) {
		this.questionList.remove(question.getId());
	}
}
