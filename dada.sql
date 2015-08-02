--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.4
-- Dumped by pg_dump version 9.4.4
-- Started on 2015-08-02 16:06:35 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

DROP DATABASE dadabase;
--
-- TOC entry 2074 (class 1262 OID 16384)
-- Name: dadabase; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE dadabase WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'fr_FR.UTF-8' LC_CTYPE = 'fr_FR.UTF-8';


ALTER DATABASE dadabase OWNER TO postgres;

\connect dadabase

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 2075 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 180 (class 3079 OID 11903)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2077 (class 0 OID 0)
-- Dependencies: 180
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 16388)
-- Name: extract_date; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE extract_date (
    id integer NOT NULL,
    id_orchestrator integer,
    status smallint
);


ALTER TABLE extract_date OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 16438)
-- Name: extract_race; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE extract_race (
    id integer NOT NULL,
    id_date integer,
    json jsonb,
    filename text,
    date_insert timestamp without time zone
);


ALTER TABLE extract_race OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 16485)
-- Name: dadajson; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW dadajson AS
 SELECT extract_race.json
   FROM extract_race
  WHERE ((extract_race.id, extract_race.id_date) IN ( SELECT max(extract_race_1.id) AS max,
            extract_race_1.id_date
           FROM extract_race extract_race_1
          WHERE (extract_race_1.id_date IN ( SELECT max(extract_date.id) AS max
                   FROM extract_date
                  GROUP BY extract_date.id_orchestrator))
          GROUP BY extract_race_1.id_date));


ALTER TABLE dadajson OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 16481)
-- Name: dadajsontext; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW dadajsontext AS
 SELECT (extract_race.json)::text AS json
   FROM extract_race
  WHERE ((extract_race.id, extract_race.id_date) IN ( SELECT max(extract_race_1.id) AS max,
            extract_race_1.id_date
           FROM extract_race extract_race_1
          WHERE (extract_race_1.id_date IN ( SELECT max(extract_date.id) AS max
                   FROM extract_date
                  GROUP BY extract_date.id_orchestrator))
          GROUP BY extract_race_1.id_date));


ALTER TABLE dadajsontext OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 16402)
-- Name: extract_date_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE extract_date_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE extract_date_id_seq OWNER TO postgres;

--
-- TOC entry 2078 (class 0 OID 0)
-- Dependencies: 173
-- Name: extract_date_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE extract_date_id_seq OWNED BY extract_date.id;


--
-- TOC entry 176 (class 1259 OID 16436)
-- Name: extract_race_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE extract_race_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE extract_race_id_seq OWNER TO postgres;

--
-- TOC entry 2079 (class 0 OID 0)
-- Dependencies: 176
-- Name: extract_race_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE extract_race_id_seq OWNED BY extract_race.id;


--
-- TOC entry 175 (class 1259 OID 16423)
-- Name: orchestrator; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE orchestrator (
    id integer NOT NULL,
    args text,
    status smallint
);


ALTER TABLE orchestrator OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 16421)
-- Name: orchestrator_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE orchestrator_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE orchestrator_id_seq OWNER TO postgres;

--
-- TOC entry 2080 (class 0 OID 0)
-- Dependencies: 174
-- Name: orchestrator_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE orchestrator_id_seq OWNED BY orchestrator.id;


--
-- TOC entry 1949 (class 2604 OID 16404)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY extract_date ALTER COLUMN id SET DEFAULT nextval('extract_date_id_seq'::regclass);


--
-- TOC entry 1951 (class 2604 OID 16441)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY extract_race ALTER COLUMN id SET DEFAULT nextval('extract_race_id_seq'::regclass);


--
-- TOC entry 1950 (class 2604 OID 16426)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY orchestrator ALTER COLUMN id SET DEFAULT nextval('orchestrator_id_seq'::regclass);


--
-- TOC entry 1958 (class 2606 OID 16447)
-- Name: extract_race_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY extract_race
    ADD CONSTRAINT extract_race_pkey PRIMARY KEY (id);


--
-- TOC entry 1953 (class 2606 OID 16409)
-- Name: linkjson_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY extract_date
    ADD CONSTRAINT linkjson_pkey PRIMARY KEY (id);


--
-- TOC entry 1955 (class 2606 OID 16431)
-- Name: orchestrator_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY orchestrator
    ADD CONSTRAINT orchestrator_pkey PRIMARY KEY (id);


--
-- TOC entry 1956 (class 1259 OID 16448)
-- Name: extract_race_json_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX extract_race_json_idx ON extract_race USING gin (json);


--
-- TOC entry 2076 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-08-02 16:06:35 CEST

--
-- PostgreSQL database dump complete
--

--
-- PostgreSQL database dump complete
--
-- View: dadajsontext

-- DROP VIEW dadajsontext;

CREATE OR REPLACE VIEW dadajsontext AS 
 SELECT extract_race.json::text AS json
   FROM extract_race
  WHERE ((extract_race.id, extract_race.id_date) IN ( SELECT max(extract_race_1.id) AS max,
            extract_race_1.id_date
           FROM extract_race extract_race_1
          WHERE (extract_race_1.id_date IN ( SELECT max(extract_date.id) AS max
                   FROM extract_date
                  GROUP BY extract_date.id_orchestrator))
          GROUP BY extract_race_1.id_date));

ALTER TABLE dadajsontext
  OWNER TO postgres;
-- View: dadajson

-- DROP VIEW dadajson;

CREATE OR REPLACE VIEW dadajson AS 
 SELECT extract_race.json
   FROM extract_race
  WHERE ((extract_race.id, extract_race.id_date) IN ( SELECT max(extract_race_1.id) AS max,
            extract_race_1.id_date
           FROM extract_race extract_race_1
          WHERE (extract_race_1.id_date IN ( SELECT max(extract_date.id) AS max
                   FROM extract_date
                  GROUP BY extract_date.id_orchestrator))
          GROUP BY extract_race_1.id_date));

ALTER TABLE dadajson
  OWNER TO postgres;


