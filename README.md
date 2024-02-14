# COMP242-Cursor-Stack

## Project Overview

This project implements an equation processor application that utilizes linked stack and cursor array implementations of a linked list to handle equations. The application can process special text files with a `.242` extension containing equations in both infix and postfix formats.

## Features

- **Equation File Processing**: Handles files starting with `<242>` tag and ending with `</242>` tag, including sections marked by `<section>` and `</section>` tags.
- **Infix and Postfix Support**: Supports optional infix and/or postfix sections within files, allowing for conversion and evaluation of equations.
- **Tag Validation**: Ensures that file tags are balanced and properly ordered.
- **Equation Navigation**: Allows users to navigate through equations in different sections using Prev and Next buttons.

## Installation

Clone the repository to your local machine:
`git clone https://github.com/MoShabaneh/COMP242-Cursor-Stack.git`

note that you must have javaFX installed to open the GUI and navigate to `src/main/java/com/example/demo/HelloApplication.java` to find the program.

## Usage

1. Run the application.
2. Click the `Load` button to open a file chooser and select an equation file (e.g., `c:\data\DS-Proj2.242`).
3. If the file is valid, its contents will be loaded and displayed. The application checks for balanced tags and loads equations from the first section.
4. Use the `Prev` and `Next` buttons to navigate through equation sections within the file.

### Equation File Format

- The file must start with `<242>` and end with `</242>`.
- Sections are defined by `<section>` and `</section>` tags.
- Equations in infix format are enclosed within `<infix>` and `</infix>` tags.
- Equations in postfix format are enclosed within `<postfix>` and `</postfix>` tags.
- Each equation is marked by `<equation>` and `</equation>` tags.

## Implementation Notes

- The linked stack is implemented using a cursor array.
- A single course array is defined, within which multiple stacks can be created as needed.
- A TextArea was used to display the equations in the GUI
