package edu.byu.cs.tweeter.client.presenter.presenters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.services.StatusService;
import edu.byu.cs.tweeter.client.presenter.InputParser;

public class MainPresenterUnitTest {

    private MainPresenter.MainView mockView;
    private StatusService mockStatusService;
    private InputParser mockInputParser;

    private MainPresenter mainPresenterSpy;
    private String post;
    private String time;
    private List<String> urls;
    private List<String> mentions;

    @Before
    public void setup() {
        //set fields
        post = "Test post";
        time = "Test time";
        urls = new ArrayList<>();
        mentions = new ArrayList<>();

        //Create mocks
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockInputParser = Mockito.mock(InputParser.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));

        //Set Presenter whens
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);
        Mockito.when(mainPresenterSpy.getParser()).thenReturn(mockInputParser);

        //Set parser whens
        try {
            Mockito.when(mockInputParser.getFormattedDateTime()).thenReturn(time);
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
            fail("getFormattedDateTime() failed");
        }

        Mockito.when(mockInputParser.parseURLs(post)).thenReturn(urls);
        Mockito.when(mockInputParser.parseMentions(post)).thenReturn(mentions);
    }

    @Test
    public void testPostStatus_handleSuccess_displaySuccessMessage() throws ParseException {
        Answer<Void> answer = new HandleResponseAnswer() {
            @Override
            void handle(MainPresenter.PostStatusObserver observer) {
                observer.handleSuccess();
            }
        };

        doStatusPost(answer);
        verifyInputParsing();

        //verify no failure
        Mockito.verify(mockView, Mockito.times(0)).displayErrorMessage(Mockito.anyString());

        //verify success
        Mockito.verify(mockView).displayPostMessage("Successfully Posted!");
    }

    @Test
    public void testPostStatus_handleFailure_displayErrorMessage() throws ParseException {
        String failMessage = "failure message";

        Answer<Void> answer = new HandleResponseAnswer() {
            @Override
            void handle(MainPresenter.PostStatusObserver observer) {
                observer.handleFailure(failMessage);
            }
        };

        verifyFailOrException(failMessage, answer, "Failed to post status: ");
    }

    @Test
    public void testPostStatus_handleException_displayExceptionMessage() throws ParseException {
        String exceptionMessage = "exception message";

        Answer<Void> answer = new HandleResponseAnswer() {
            @Override
            void handle(MainPresenter.PostStatusObserver observer) {
                observer.handleException(new Exception(exceptionMessage));
            }
        };

        verifyFailOrException(exceptionMessage, answer, "Failed to post status because of exception: ");
    }

    protected abstract class HandleResponseAnswer implements Answer<Void> {
        @Override
        public Void answer(InvocationOnMock invocation) {
            String post = invocation.getArgument(0);
            String time = invocation.getArgument(1);
            List<String> urls = invocation.getArgument(2);
            List<String> mentions = invocation.getArgument(3);
            MainPresenter.PostStatusObserver observer = invocation.getArgument(4, MainPresenter.PostStatusObserver.class);

            //Verify correct arguments were passed
            assertEquals(MainPresenterUnitTest.this.post, post);
            assertEquals(MainPresenterUnitTest.this.time, time);
            assertEquals(MainPresenterUnitTest.this.urls, urls);
            assertEquals(MainPresenterUnitTest.this.mentions, mentions);
            assertNotNull(observer);

            handle(observer);
            return null;
        }

        abstract void handle(MainPresenter.PostStatusObserver observer);
    }

    private void doStatusPost(Answer<Void> answer) {
        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

        mainPresenterSpy.postStatus(post);
    }

    private void verifyInputParsing() throws ParseException {
        //verify input parsing
        Mockito.verify(mockInputParser).getFormattedDateTime();
        Mockito.verify(mockInputParser).parseURLs(post);
        Mockito.verify(mockInputParser).parseMentions(post);
    }

    private void verifyFailOrException(String failMessage, Answer<Void> answer, String s) throws ParseException {
        doStatusPost(answer);
        verifyInputParsing();
        Mockito.verify(mockView, Mockito.times(0)).displayPostMessage("Successfully Posted!");
        Mockito.verify(mockView).displayErrorMessage(s + failMessage);
    }
}