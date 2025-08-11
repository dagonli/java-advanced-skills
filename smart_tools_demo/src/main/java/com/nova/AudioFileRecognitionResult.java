package com.nova;



import java.util.List;

/**
 * @author zouxiaoliang
 * @date 2025/5/17 15:36
 */

public class AudioFileRecognitionResult {

    private Double duration;

    private String text;
    private List<Utterance> utterances;


    public static class Utterance {
        /**
         * 是否分句最终结果
         */
        private Attribute attribute;
        private boolean definite;
        private int end_time;
        private int start_time;
        private String text;
        private List<Word> words;

        public Attribute getAttribute() {
            return attribute;
        }

        public void setAttribute(Attribute attribute) {
            this.attribute = attribute;
        }

        public boolean isDefinite() {
            return definite;
        }

        public void setDefinite(boolean definite) {
            this.definite = definite;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<Word> getWords() {
            return words;
        }

        public void setWords(List<Word> words) {
            this.words = words;
        }

        public static class Attribute {
            private String speaker;

            public String getSpeaker() {
                return speaker;
            }

            public void setSpeaker(String speaker) {
                this.speaker = speaker;
            }
        }


        public static class Word {
            private int end_time;
            private int start_time;
            private String text;

            public int getEnd_time() {
                return end_time;
            }

            public void setEnd_time(int end_time) {
                this.end_time = end_time;
            }

            public int getStart_time() {
                return start_time;
            }

            public void setStart_time(int start_time) {
                this.start_time = start_time;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Utterance> getUtterances() {
        return utterances;
    }

    public void setUtterances(List<Utterance> utterances) {
        this.utterances = utterances;
    }
}
