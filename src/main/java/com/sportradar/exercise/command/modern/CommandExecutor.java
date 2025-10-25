package com.sportradar.exercise.command.modern;

import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
public class CommandExecutor {
    private final Stack<MatchCommand<?>> commandHistory = new Stack<>();

    public <T> T executeCommand(MatchCommand<T> command) {
        T result = command.execute();
        commandHistory.push(command);
        return result;
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            MatchCommand<?> lastCommand = commandHistory.pop();
            lastCommand.undo();
        }
    }

    public void clearHistory() {
        commandHistory.clear();
    }

    public int getHistorySize() {
        return commandHistory.size();
    }
}