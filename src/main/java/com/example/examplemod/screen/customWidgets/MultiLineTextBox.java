package com.example.examplemod.screen.customWidgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class MultiLineTextBox extends AbstractWidget {
    private final Font font;
    private String text = "";
    private int cursorPosition = 0;
    private int selectionPos = 0;
    private int scrollOffset = 0;
    private List<VisualLine> visualLines = new ArrayList<>();

    private static class VisualLine {
        String text;
        int originalStartPos;
        int originalEndPos;

        VisualLine(String text, int startPos, int endPos) {
            this.text = text;
            this.originalStartPos = startPos;
            this.originalEndPos = endPos;
        }
    }

    public MultiLineTextBox(Font font, int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
        this.font = font;
        updateVisualLines();
    }

    private void updateVisualLines() {
        visualLines.clear();

        String[] rawLines = text.split("\n", -1);
        int globalPos = 0;

        for (String line : rawLines) {
            String remaining = line;
            int lineStart = globalPos;

            while (!remaining.isEmpty()) {
                int charsToTake = getCharsThatFitWidth(remaining, width - 8);
                if (charsToTake == 0) charsToTake = 1;

                String visualText = remaining.substring(0, charsToTake);
                visualLines.add(new VisualLine(visualText, globalPos, globalPos + visualText.length()));

                globalPos += visualText.length();
                remaining = remaining.substring(charsToTake);
            }

            if (rawLines.length > 1 && line != rawLines[rawLines.length - 1]) {
                globalPos++;
            }
        }
    }

    // Собственная реализация getSplitOffset
    private int getSplitOffset(String text, int maxWidth, int targetX) {
        if (text.isEmpty()) return 0;

        int low = 0;
        int high = text.length();
        int best = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            String subText = text.substring(0, mid);
            int width = font.width(subText);

            if (width <= targetX) {
                best = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return best;
    }

    private int getCharsThatFitWidth(String text, int maxWidth) {
        if (text.isEmpty()) return 0;

        int low = 0;
        int high = text.length();
        int best = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            String subText = text.substring(0, mid);
            int width = font.width(subText);

            if (width <= maxWidth) {
                best = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return best;
    }

    private VisualLine getVisualLineAtCursor() {
        if (visualLines.isEmpty()) return null;

        for (int i = 0; i < visualLines.size(); i++) {
            VisualLine vl = visualLines.get(i);
            if (cursorPosition >= vl.originalStartPos && cursorPosition <= vl.originalEndPos) {
                return vl;
            }
            if (i < visualLines.size() - 1 &&
                    cursorPosition > vl.originalEndPos &&
                    cursorPosition < visualLines.get(i + 1).originalStartPos) {
                return vl;
            }
        }

        if (cursorPosition >= text.length() && !visualLines.isEmpty()) {
            return visualLines.get(visualLines.size() - 1);
        }

        return visualLines.isEmpty() ? null : visualLines.get(0);
    }

    private int getVisualLineIndex() {
        for (int i = 0; i < visualLines.size(); i++) {
            VisualLine vl = visualLines.get(i);
            if (cursorPosition >= vl.originalStartPos && cursorPosition <= vl.originalEndPos) {
                return i;
            }
            if (i < visualLines.size() - 1 &&
                    cursorPosition > vl.originalEndPos &&
                    cursorPosition < visualLines.get(i + 1).originalStartPos) {
                return i;
            }
        }
        return visualLines.isEmpty() ? 0 : visualLines.size() - 1;
    }

    private int getCursorXInVisualLine(VisualLine vl) {
        int offsetInLine = cursorPosition - vl.originalStartPos;
        if (offsetInLine < 0) offsetInLine = 0;
        if (offsetInLine > vl.text.length()) offsetInLine = vl.text.length();

        String textBeforeCursor = vl.text.substring(0, offsetInLine);
        return getX() + 4 + font.width(textBeforeCursor);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        updateVisualLines();

        guiGraphics.fill(getX(), getY(), getX() + width, getY() + height,
                isFocused() ? 0xFF444444 : 0xFF222222);
        guiGraphics.renderOutline(getX(), getY(), width, height, 0xFF888888);

        int lineHeight = 9;
        int maxVisibleLines = (height - 8) / lineHeight;

        int cursorLineIndex = getVisualLineIndex();
        if (cursorLineIndex < scrollOffset) {
            scrollOffset = cursorLineIndex;
        } else if (cursorLineIndex >= scrollOffset + maxVisibleLines) {
            scrollOffset = cursorLineIndex - maxVisibleLines + 1;
        }

        int currentY = getY() + 4;
        for (int i = scrollOffset; i < Math.min(visualLines.size(), scrollOffset + maxVisibleLines); i++) {
            VisualLine vl = visualLines.get(i);
            guiGraphics.drawString(font, vl.text, getX() + 4, currentY, 0xFFFFFF);
            currentY += lineHeight;
        }

        if (isFocused() && (System.currentTimeMillis() / 500 % 2 == 0)) {
            VisualLine cursorLine = getVisualLineAtCursor();
            if (cursorLine != null) {
                int cursorLineIndexForDraw = getVisualLineIndex();
                if (cursorLineIndexForDraw >= scrollOffset &&
                        cursorLineIndexForDraw < scrollOffset + maxVisibleLines) {

                    int cursorX = getCursorXInVisualLine(cursorLine);
                    int cursorY = getY() + 4 + (cursorLineIndexForDraw - scrollOffset) * lineHeight;
                    guiGraphics.fill(cursorX, cursorY, cursorX + 1, cursorY + lineHeight, 0xFFFFFFFF);
                }
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isFocused()) return false;

        boolean shiftPressed = (modifiers & 1) != 0;

        switch (keyCode) {
            case GLFW.GLFW_KEY_ENTER:
            case GLFW.GLFW_KEY_KP_ENTER:
                insertText("\n");
                return true;

            case GLFW.GLFW_KEY_BACKSPACE:
                if (cursorPosition > 0) {
                    if (shiftPressed && selectionPos != cursorPosition) {
                        int start = Math.min(cursorPosition, selectionPos);
                        int end = Math.max(cursorPosition, selectionPos);
                        text = text.substring(0, start) + text.substring(end);
                        cursorPosition = start;
                        selectionPos = start;
                    } else {
                        text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
                        cursorPosition--;
                        selectionPos = cursorPosition;
                    }
                    updateVisualLines();
                }
                return true;

            case GLFW.GLFW_KEY_DELETE:
                if (cursorPosition < text.length()) {
                    if (shiftPressed && selectionPos != cursorPosition) {
                        int start = Math.min(cursorPosition, selectionPos);
                        int end = Math.max(cursorPosition, selectionPos);
                        text = text.substring(0, start) + text.substring(end);
                        cursorPosition = start;
                        selectionPos = start;
                    } else {
                        text = text.substring(0, cursorPosition) + text.substring(cursorPosition + 1);
                    }
                    updateVisualLines();
                }
                return true;

            case GLFW.GLFW_KEY_LEFT:
                if (cursorPosition > 0) {
                    cursorPosition--;
                    if (!shiftPressed) selectionPos = cursorPosition;
                }
                return true;

            case GLFW.GLFW_KEY_RIGHT:
                if (cursorPosition < text.length()) {
                    cursorPosition++;
                    if (!shiftPressed) selectionPos = cursorPosition;
                }
                return true;

            case GLFW.GLFW_KEY_UP:
                moveCursorUp();
                if (!shiftPressed) selectionPos = cursorPosition;
                return true;

            case GLFW.GLFW_KEY_DOWN:
                moveCursorDown();
                if (!shiftPressed) selectionPos = cursorPosition;
                return true;

            case GLFW.GLFW_KEY_HOME:
                int lineStart = getLogicalLineStart(cursorPosition);
                cursorPosition = lineStart;
                if (!shiftPressed) selectionPos = cursorPosition;
                return true;

            case GLFW.GLFW_KEY_END:
                int lineEnd = getLogicalLineEnd(cursorPosition);
                cursorPosition = lineEnd;
                if (!shiftPressed) selectionPos = cursorPosition;
                return true;
        }

        return false;
    }

    private void moveCursorUp() {
        if (visualLines.isEmpty()) return;

        int currentLineIndex = getVisualLineIndex();
        if (currentLineIndex > 0) {
            VisualLine currentLine = visualLines.get(currentLineIndex);
            int cursorXInLine = getCursorXInVisualLine(currentLine);

            VisualLine aboveLine = visualLines.get(currentLineIndex - 1);
            int newPosInAboveLine = getSplitOffset(aboveLine.text, font.width(aboveLine.text), cursorXInLine - getX() - 4);
            newPosInAboveLine = Math.min(newPosInAboveLine, aboveLine.text.length());

            cursorPosition = aboveLine.originalStartPos + newPosInAboveLine;
        }
    }

    private void moveCursorDown() {
        if (visualLines.isEmpty()) return;

        int currentLineIndex = getVisualLineIndex();
        if (currentLineIndex < visualLines.size() - 1) {
            VisualLine currentLine = visualLines.get(currentLineIndex);
            int cursorXInLine = getCursorXInVisualLine(currentLine);

            VisualLine belowLine = visualLines.get(currentLineIndex + 1);
            int newPosInBelowLine = getSplitOffset(belowLine.text, font.width(belowLine.text), cursorXInLine - getX() - 4);
            newPosInBelowLine = Math.min(newPosInBelowLine, belowLine.text.length());

            cursorPosition = belowLine.originalStartPos + newPosInBelowLine;
        }
    }

    private int getLogicalLineStart(int pos) {
        int lastNewLine = text.lastIndexOf('\n', pos - 1);
        return lastNewLine + 1;
    }

    private int getLogicalLineEnd(int pos) {
        int nextNewLine = text.indexOf('\n', pos);
        if (nextNewLine == -1) return text.length();
        return nextNewLine;
    }

    private void insertText(String insertStr) {
        if (selectionPos != cursorPosition) {
            int start = Math.min(cursorPosition, selectionPos);
            int end = Math.max(cursorPosition, selectionPos);
            text = text.substring(0, start) + insertStr + text.substring(end);
            cursorPosition = start + insertStr.length();
            selectionPos = cursorPosition;
        } else {
            text = text.substring(0, cursorPosition) + insertStr + text.substring(cursorPosition);
            cursorPosition += insertStr.length();
            selectionPos = cursorPosition;
        }
        updateVisualLines();
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (isFocused() && Character.isDefined(codePoint) && !Character.isISOControl(codePoint)) {
            insertText(String.valueOf(codePoint));
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isFocused() && isMouseOver(mouseX, mouseY)) {
            int lineHeight = 9;
            int relativeY = (int)(mouseY - getY() - 4);
            int lineIndex = relativeY / lineHeight + scrollOffset;

            if (lineIndex >= 0 && lineIndex < visualLines.size()) {
                VisualLine clickedLine = visualLines.get(lineIndex);
                int relativeX = (int)(mouseX - getX() - 4);
                int clickPosInLine = getSplitOffset(clickedLine.text, font.width(clickedLine.text), relativeX);
                clickPosInLine = Math.min(clickPosInLine, clickedLine.text.length());

                cursorPosition = clickedLine.originalStartPos + clickPosInLine;
                selectionPos = cursorPosition;
            }
            return true;
        }
        return false;
    }

    public String getValue() {
        return text;
    }

    public void setValue(String text) {
        this.text = text;
        this.cursorPosition = Math.min(cursorPosition, text.length());
        this.selectionPos = cursorPosition;
        updateVisualLines();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}