--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.4
-- Dumped by pg_dump version 9.4.4
-- Started on 2015-08-03 11:42:35 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

DROP DATABASE dadabase;
--
-- TOC entry 2084 (class 1262 OID 16384)
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
-- TOC entry 2085 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 182 (class 3079 OID 11903)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2087 (class 0 OID 0)
-- Dependencies: 182
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

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
-- TOC entry 181 (class 1259 OID 16493)
-- Name: dadahorse; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW dadahorse AS
 SELECT DISTINCT test.horse_name,
    test.horse_age,
    test.horse_sex,
    test.horse_last_result
   FROM ( SELECT (t.json ->> 'date'::text) AS date,
            (res.value #>> '{horse,name}'::text[]) AS horse_name,
            (res.value #>> '{horse,age}'::text[]) AS horse_age,
            (res.value #>> '{horse,sex}'::text[]) AS horse_sex,
            (res.value #>> '{horse,lastResults}'::text[]) AS horse_last_result
           FROM extract_race t,
            LATERAL jsonb_array_elements((t.json -> 'results'::text)) res(value)
          ORDER BY (t.json ->> 'date'::text)) test;


ALTER TABLE dadahorse OWNER TO postgres;

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
-- TOC entry 179 (class 1259 OID 16485)
-- Name: dadajson; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW dadajson AS
 SELECT extract_race.json
   FROM extract_race
  WHERE (extract_race.id_date IN ( SELECT max(extract_date.id) AS max
           FROM extract_date
          GROUP BY extract_date.id_orchestrator));


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
-- TOC entry 180 (class 1259 OID 16489)
-- Name: dadarace; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW dadarace AS
 SELECT (t.json ->> 'id'::text) AS id,
    (t.json ->> 'name'::text) AS race,
    (t.json ->> 'date'::text) AS date,
    (res.value ->> 'cote'::text) AS cote,
    (res.value ->> 'jockey'::text) AS jockey,
    (res.value ->> 'place'::text) AS place,
    (res.value #>> '{horse,name}'::text[]) AS horse_name,
    (res.value #>> '{horse,age}'::text[]) AS horse_age,
    (res.value #>> '{horse,sex}'::text[]) AS horse_sex,
    (res.value #>> '{horse,lastResults}'::text[]) AS horse_last_result
   FROM dadajson t,
    LATERAL jsonb_array_elements((t.json -> 'results'::text)) res(value);


ALTER TABLE dadarace OWNER TO postgres;

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
-- TOC entry 2088 (class 0 OID 0)
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
-- TOC entry 2089 (class 0 OID 0)
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
-- TOC entry 2090 (class 0 OID 0)
-- Dependencies: 174
-- Name: orchestrator_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE orchestrator_id_seq OWNED BY orchestrator.id;


--
-- TOC entry 1957 (class 2604 OID 16404)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY extract_date ALTER COLUMN id SET DEFAULT nextval('extract_date_id_seq'::regclass);


--
-- TOC entry 1959 (class 2604 OID 16441)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY extract_race ALTER COLUMN id SET DEFAULT nextval('extract_race_id_seq'::regclass);


--
-- TOC entry 1958 (class 2604 OID 16426)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY orchestrator ALTER COLUMN id SET DEFAULT nextval('orchestrator_id_seq'::regclass);


--
-- TOC entry 1966 (class 2606 OID 16447)
-- Name: extract_race_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY extract_race
    ADD CONSTRAINT extract_race_pkey PRIMARY KEY (id);


--
-- TOC entry 1961 (class 2606 OID 16409)
-- Name: linkjson_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY extract_date
    ADD CONSTRAINT linkjson_pkey PRIMARY KEY (id);


--
-- TOC entry 1963 (class 2606 OID 16431)
-- Name: orchestrator_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY orchestrator
    ADD CONSTRAINT orchestrator_pkey PRIMARY KEY (id);


--
-- TOC entry 1964 (class 1259 OID 16448)
-- Name: extract_race_json_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX extract_race_json_idx ON extract_race USING gin (json);


--
-- TOC entry 2086 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-08-03 11:42:35 CEST

--
-- PostgreSQL database dump complete
--

