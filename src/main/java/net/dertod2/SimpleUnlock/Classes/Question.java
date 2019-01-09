package net.dertod2.SimpleUnlock.Classes;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.dertod2.DatabaseHandler.Binary.DatabaseHandler;
import net.dertod2.DatabaseHandler.Table.Column;
import net.dertod2.DatabaseHandler.Table.Column.ColumnType;
import net.dertod2.DatabaseHandler.Table.Column.DataType;
import net.dertod2.DatabaseHandler.Table.Column.EntryType;
import net.dertod2.DatabaseHandler.Table.TableEntry;
import net.dertod2.DatabaseHandler.Table.TableInfo;
import net.dertod2.SimpleUnlock.Binary.SimpleUnlock;

@TableInfo(tableName = "su_questions")
public class Question extends TableEntry {
    @Column(columnName = "id", dataType = DataType.Integer, columnType = ColumnType.Primary, order = 1)
    private int id;

    @Column(columnName = "question", dataType = DataType.String, order = 2)
    private String question;

    @Column(columnName = "answers", dataType = DataType.String, entryType = EntryType.List, order = 4)
    private List<String> answerOptions;
    @Column(columnName = "correct_answer", dataType = DataType.Integer, order = 3)
    private int correctAnswer;

    public Question() {
    }

    public Question(String question) {
        this.question = question;
        this.answerOptions = new ArrayList<String>();
    }

    public static Question getQuestion(int questionId) {
        return SimpleUnlock.unlockControl.getQuestion(questionId);
    }

    public static List<Question> getQuestions() {
        return SimpleUnlock.unlockControl.getQuestions();
    }

    @SuppressWarnings("deprecation")
    public static Question addQuestion(String question) {
        Question newQuestion = new Question(question);

        try {
            DatabaseHandler.getHandler().insert(newQuestion);
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }

        SimpleUnlock.unlockControl.add(newQuestion);
        return newQuestion;
    }

    public Question getInstance() {
        return new Question();
    }

    public int getId() {
        return this.id;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
        this.update();
    }

    public List<String> getAnswers() {
        return ImmutableList.<String>copyOf(this.answerOptions);
    }

    public void addAnswer(String answer) {
        this.answerOptions.add(answer);
        this.update();
    }

    public int getCorrectAnswer() {
        return this.correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
        this.update();
    }

    public int getMaxChances() {
        int chances = (int) ((this.answerOptions.size() * SimpleUnlock.getConfiguration().getDouble("chances-mod"))
                / 100);

        if (chances < SimpleUnlock.getConfiguration().getInt("min-chances"))
            chances = SimpleUnlock.getConfiguration().getInt("min-chances");
        if (chances > SimpleUnlock.getConfiguration().getInt("max-chances"))
            chances = SimpleUnlock.getConfiguration().getInt("max-chances");

        return chances;
    }

    @SuppressWarnings("deprecation")
    public void update() {
        try {
            DatabaseHandler.getHandler().update(this);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void delete() {
        try {
            DatabaseHandler.getHandler().remove(this);
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            SimpleUnlock.unlockControl.delete(this);
        }
    }
}
