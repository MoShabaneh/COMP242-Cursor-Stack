package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;


public class HelloApplication extends Application {
    private CursorStack<String> sectionStack = new CursorStack<>();
    private int currentSection = 0;

    public static void main(String[] args) {
        launch(args);
    }

    Label fileLabel = new Label("Selected File: ");
    TextArea equationTextArea = new TextArea();
    String[] sections;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Equation Viewer");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label equationLabel = new Label("Equation: ");
        equationTextArea.setWrapText(true); // Allow wrapping of text within the TextArea

        Button loadButton = new Button("Load");
        loadButton.setOnAction(e -> {
            loadFile();
        });

        Button prevButton = new Button("Prev");
        prevButton.setOnAction(e -> {
            if (currentSection > 1) {
                currentSection--;
                equationTextArea.setText(sections[currentSection]);
            } else {
                // alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No Previous Section");
                alert.setContentText("There is no previous section.");
                alert.showAndWait();
            }
        });

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> {
            if (currentSection < sections.length - 1) {
                currentSection++;
                equationTextArea.setText(sections[currentSection]);
            } else {
                // alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No Next Section");
                alert.setContentText("There is no next section.");
                alert.showAndWait();
            }
        });
        HBox fileBox = new HBox(10);
        fileBox.getChildren().addAll(fileLabel, loadButton);
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(prevButton, nextButton);
        layout.getChildren().addAll(fileBox, equationLabel, equationTextArea, buttonBox);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static boolean isValid(File selectedFile) throws IOException {
        boolean valid = true;
        CursorStack<String> stack = new CursorStack<>();
        FileReader fileReader = new FileReader(selectedFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains("<242>")) {
                stack.push("<242>");
            } else if (line.contains("</242>") && stack.peek().equals("<242>")) {
                stack.pop();
            } else if (line.contains("<section>")) {
                stack.push("<section>");
            } else if (line.contains("</section>") && stack.peek().equals("<section>")) {
                stack.pop();
            } else if (line.contains("<infix>")) {
                stack.push("<infix>");
            } else if (line.contains("</infix>") && stack.peek().equals("<infix>")) {
                stack.pop();
            } else if (line.contains("<postfix>")) {
                stack.push("<postfix>");
            } else if (line.contains("</postfix>") && stack.peek().equals("<postfix>")) {
                stack.pop();
            } else if (line.contains("<equation>") && line.contains("</equation>")) {

            } else {
                //valid = false;
            }
        }
        return valid;
    }

    private void loadFile() {
        currentSection = 0;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Equation File");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fileLabel.setText("Selected File: " + selectedFile.getName());
            try {
                if (!isValid(selectedFile)) {
                    // error alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid File");
                    alert.setContentText("The selected file is invalid.");
                    alert.showAndWait();
                    return;
                }
                FileReader fileReader = new FileReader(selectedFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                String line;
                String output = "";
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("<section>")) {
                        output += "this is section";
                        sectionStack.push("<section>");
                        while ((line = bufferedReader.readLine()) != null && !line.contains("</section>")) {
                            if (line.contains("<infix>")) {
                                output += "Infix: \n";
                                while ((line = bufferedReader.readLine()) != null && !line.contains("</infix>")) {
                                    if (line.contains("<equation>") && line.contains("</equation>")) {
                                        String equation = line.substring(line.indexOf("<equation>") + 10, line.indexOf("</equation>")).trim();
                                        output += equation + " ==> " + infixToPostfix(equation) + " ==> " + evaluatePostfix(infixToPostfix(equation)) + "\n";
                                    }
                                }
                            }
                            if (line.contains("<postfix>")) {
                                output += "Postfix: \n";
                                while ((line = bufferedReader.readLine()) != null && !line.contains("</postfix>")) {
                                    if (line.contains("<equation>") && line.contains("</equation>")) {
                                        String equation = line.substring(line.indexOf("<equation>") + 10, line.indexOf("</equation>")).trim();
                                        try {
                                            output += equation + " ==> " + postfixToPrefix(equation) + " ==> " + evaluatePrefix(postfixToPrefix(equation)) + "\n";
                                        } catch (Exception e) {
                                            output += "Not valid Equation\n";
                                        }
                                    }
                                }
                            }
                        }

                    } else if (line.contains("</section>")) {
                        while (!sectionStack.isEmpty() && !sectionStack.peek().equals("<section>")) {
                            output += sectionStack.pop();
                            sectionStack.pop();
                        }
                    }
                }

                sections = output.split("this is section");
                equationTextArea.setText(sections[1]);
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No File Selected");
            alert.setContentText("Please select a file.");
            alert.showAndWait();
        }
    }

    public static String infixToPostfix(String infix) {
        String post = "";

        CursorStack<String> stack = new CursorStack<String>();
        String[] split = infix.split(" ");

        for (int i = 0; i < split.length; i++) {

            if (isNumeric(split[i])) {// checks whether it is a number and adds it to the result
                post += split[i] + " ";
            } else if (split[i].equals("(")) {// pushes '(' to the stack
                stack.push(split[i]);
            } else if (split[i].equals(")")) {
                // empty
                while (!stack.isEmpty() && stack.peek() != "(") {
                    post += stack.pop() + " "; // adds the operator to the result statement
                }
                stack.pop();
            } else {
                while (!stack.isEmpty() && Precedence(split[i]) <= Precedence(stack.peek())) {
                    post += stack.pop() + " ";
                }
                stack.push(split[i]);
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peek() == "(" || stack.peek() == ")")
                return null;
            post += stack.pop() + " ";
        }
        post = post.replaceAll("<[^>]*>", "");
        return post.replaceAll("[()]", "");
    }

    static int Precedence(String ch) {
        switch (ch) {
            case "+":
                return 1;
            case "-":
                return 1;
            case "*":
                return 2;
            case "^":
                return 2;
            case "/":
                return 2;

            default:
                return -1;
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static double evaluatePostfix(String expression) {
        //create a stack
        CursorStack<Double> stack = new CursorStack<>();
        String[] tokens = expression.split(" ");

        //for
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals(""))
                continue;
            if (Character.isDigit(tokens[i].charAt(0))) {
                stack.push(Double.parseDouble(tokens[i]));
            }

            //else
            else {
                double val1 = stack.pop();
                double val2 = stack.pop();

                switch (tokens[i].charAt(0)) {
                    case '+':
                        stack.push(val2 + val1);
                        break;

                    case '-':
                        stack.push(val2 - val1);
                        break;

                    case '/':
                        stack.push(val2 / val1);
                        break;

                    case '*':
                        stack.push(val2 * val1);
                        break;

                    case '^':
                        stack.push(Math.pow(val2, val1));
                        break;
                }
            }
        }

        return stack.pop();
    }

    public static String postfixToPrefix(String postfix) {
        CursorStack<String> stack = new CursorStack<String>();
        String[] split = postfix.split(" ");

        for (int i = 0; i < split.length; i++) {
            if (isNumeric(split[i])) {
                stack.push(split[i]);
            } else {
                try {
                    String operand1 = stack.pop();
                    String operand2;
                    if (stack.peek() == null) {
                        operand2 = "";
                    } else {
                        operand2 = stack.pop();
                    }
                    String temp = split[i] + " " + operand2 + " " + operand1;
                    stack.push(temp);
                } catch (Exception e) {
                    return null;
                }
            }
        }

        if (stack.isEmpty()) {
            return null; // Invalid postfix expression
        }

        return stack.pop();
    }

    public static double evaluatePrefix(String prefix) {
        CursorStack<Double> stack = new CursorStack<Double>();

        // we assume prefix is space separated
        String[] tokens = prefix.split(" ");
        for (int i = tokens.length - 1; i >= 0; i--) {
            String token = tokens[i];
            if (isNumeric(token)) {
                //  push to stack
                stack.push(Double.parseDouble(token));
            } else {
                double operand1;
                double operand2;
                if (stack.peek() == null) {
                    operand1 = 0;
                } else {
                    operand1 = stack.pop();
                }
                if (stack.peek() == null) {
                    operand2 = 0;
                } else {
                    operand2 = stack.pop();
                }
                switch (token) {
                    case "+":
                        stack.push(operand1 + operand2);
                        break;
                    case "-":
                        stack.push(operand1 - operand2);
                        break;
                    case "*":
                        stack.push(operand1 * operand2);
                        break;
                    case "/":
                        if (operand2 == 0.0) {
                            throw new IllegalArgumentException("Division by zero");
                        }
                        stack.push(operand1 / operand2);
                        break;
                }
            }
        }

        if (stack.isEmpty()) {
            throw new IllegalArgumentException("Invalid prefix expression");
        }

        return stack.pop();
    }

}

