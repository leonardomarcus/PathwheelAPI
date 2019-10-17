--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.11
-- Dumped by pg_dump version 9.6.11

-- Started on 2019-10-16 17:22:38 BRT

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 14 (class 2615 OID 16386)
-- Name: pathwheel; Type: SCHEMA; Schema: -; Owner: pathwheel
--

CREATE SCHEMA pathwheel;


ALTER SCHEMA pathwheel OWNER TO pathwheel;

--
-- TOC entry 15 (class 2615 OID 16387)
-- Name: tiger; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA tiger;


ALTER SCHEMA tiger OWNER TO postgres;

--
-- TOC entry 16 (class 2615 OID 18141)
-- Name: tiger_data; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA tiger_data;


ALTER SCHEMA tiger_data OWNER TO postgres;

--
-- TOC entry 17 (class 2615 OID 18300)
-- Name: topology; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA topology;


ALTER SCHEMA topology OWNER TO postgres;

--
-- TOC entry 1 (class 3079 OID 12393)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 4189 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 6 (class 3079 OID 18301)
-- Name: address_standardizer; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS address_standardizer WITH SCHEMA public;


--
-- TOC entry 4190 (class 0 OID 0)
-- Dependencies: 6
-- Name: EXTENSION address_standardizer; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION address_standardizer IS 'Used to parse an address into constituent elements. Generally used to support geocoding address normalization step.';


--
-- TOC entry 5 (class 3079 OID 18308)
-- Name: address_standardizer_data_us; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS address_standardizer_data_us WITH SCHEMA public;


--
-- TOC entry 4191 (class 0 OID 0)
-- Dependencies: 5
-- Name: EXTENSION address_standardizer_data_us; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION address_standardizer_data_us IS 'Address Standardizer US dataset example';


--
-- TOC entry 8 (class 3079 OID 16388)
-- Name: fuzzystrmatch; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS fuzzystrmatch WITH SCHEMA public;


--
-- TOC entry 4192 (class 0 OID 0)
-- Dependencies: 8
-- Name: EXTENSION fuzzystrmatch; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION fuzzystrmatch IS 'determine similarities and distance between strings';


--
-- TOC entry 7 (class 3079 OID 16399)
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- TOC entry 4193 (class 0 OID 0)
-- Dependencies: 7
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


--
-- TOC entry 4 (class 3079 OID 18351)
-- Name: postgis_sfcgal; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis_sfcgal WITH SCHEMA public;


--
-- TOC entry 4194 (class 0 OID 0)
-- Dependencies: 4
-- Name: EXTENSION postgis_sfcgal; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis_sfcgal IS 'PostGIS SFCGAL functions';


--
-- TOC entry 2 (class 3079 OID 17872)
-- Name: postgis_tiger_geocoder; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis_tiger_geocoder WITH SCHEMA tiger;


--
-- TOC entry 4195 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION postgis_tiger_geocoder; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis_tiger_geocoder IS 'PostGIS tiger geocoder and reverse geocoder';


--
-- TOC entry 3 (class 3079 OID 18369)
-- Name: postgis_topology; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis_topology WITH SCHEMA topology;


--
-- TOC entry 4196 (class 0 OID 0)
-- Dependencies: 3
-- Name: EXTENSION postgis_topology; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis_topology IS 'PostGIS topology spatial types and functions';



SET default_tablespace = '';

SET default_with_oids = false;



CREATE TABLE pathwheel.achievement (
    id integer,
    title character varying,
    description character varying
);


ALTER TABLE pathwheel.achievement OWNER TO pathwheel;

--
-- TOC entry 294 (class 1259 OID 24600)
-- Name: email_box; Type: TABLE; Schema: pathwheel; Owner: pathwheel
--

CREATE TABLE pathwheel.email_box (
);


ALTER TABLE pathwheel.email_box OWNER TO pathwheel;

--
-- TOC entry 276 (class 1259 OID 18520)
-- Name: seq_log_authentication; Type: SEQUENCE; Schema: pathwheel; Owner: pathwheel
--

CREATE SEQUENCE pathwheel.seq_log_authentication
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pathwheel.seq_log_authentication OWNER TO pathwheel;

--
-- TOC entry 277 (class 1259 OID 18522)
-- Name: log_authentication; Type: TABLE; Schema: pathwheel; Owner: pathwheel
--

CREATE TABLE pathwheel.log_authentication (
    id bigint DEFAULT nextval('pathwheel.seq_log_authentication'::regclass) NOT NULL,
    user_id bigint,
    registration_date timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);


ALTER TABLE pathwheel.log_authentication OWNER TO pathwheel;

--
-- TOC entry 278 (class 1259 OID 18527)
-- Name: seq_pavement_sample; Type: SEQUENCE; Schema: pathwheel; Owner: pathwheel
--

CREATE SEQUENCE pathwheel.seq_pavement_sample
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pathwheel.seq_pavement_sample OWNER TO pathwheel;

--
-- TOC entry 279 (class 1259 OID 18529)
-- Name: pavement_sample; Type: TABLE; Schema: pathwheel; Owner: pathwheel
--

CREATE TABLE pathwheel.pavement_sample (
    id bigint DEFAULT nextval('pathwheel.seq_pavement_sample'::regclass) NOT NULL,
    registration_date timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    latitude_init numeric NOT NULL,
    longitude_init numeric NOT NULL,
    latitude_end numeric NOT NULL,
    longitude_end numeric NOT NULL,
    elapsed_time numeric NOT NULL,
    vertical_acceleration numeric NOT NULL,
    speed numeric NOT NULL,
    distance numeric NOT NULL,
    accuracy numeric NOT NULL,
    user_id bigint NOT NULL,
    steps integer,
    smart_device_id integer,
    exclusion_date timestamp without time zone,
    travel_mode_id integer
);


ALTER TABLE pathwheel.pavement_sample OWNER TO pathwheel;


--
-- TOC entry 284 (class 1259 OID 18556)
-- Name: pavement_type; Type: TABLE; Schema: pathwheel; Owner: pathwheel
--

CREATE TABLE pathwheel.pavement_type (
    id integer NOT NULL,
    description character varying
);


ALTER TABLE pathwheel.pavement_type OWNER TO pathwheel;

--
-- TOC entry 297 (class 1259 OID 24608)
-- Name: seq_achievement; Type: SEQUENCE; Schema: pathwheel; Owner: pathwheel
--

CREATE SEQUENCE pathwheel.seq_achievement
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pathwheel.seq_achievement OWNER TO pathwheel;

--
-- TOC entry 295 (class 1259 OID 24603)
-- Name: seq_email_box; Type: SEQUENCE; Schema: pathwheel; Owner: pathwheel
--

CREATE SEQUENCE pathwheel.seq_email_box
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pathwheel.seq_email_box OWNER TO pathwheel;

--
-- TOC entry 285 (class 1259 OID 18562)
-- Name: seq_smart_device; Type: SEQUENCE; Schema: pathwheel; Owner: pathwheel
--

CREATE SEQUENCE pathwheel.seq_smart_device
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pathwheel.seq_smart_device OWNER TO pathwheel;

--
-- TOC entry 286 (class 1259 OID 18564)
-- Name: seq_spot; Type: SEQUENCE; Schema: pathwheel; Owner: pathwheel
--

CREATE SEQUENCE pathwheel.seq_spot
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pathwheel.seq_spot OWNER TO pathwheel;

--
-- TOC entry 287 (class 1259 OID 18566)
-- Name: seq_user; Type: SEQUENCE; Schema: pathwheel; Owner: pathwheel
--

CREATE SEQUENCE pathwheel.seq_user
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pathwheel.seq_user OWNER TO pathwheel;

--
-- TOC entry 288 (class 1259 OID 18568)
-- Name: seq_wheelchair; Type: SEQUENCE; Schema: pathwheel; Owner: pathwheel
--

CREATE SEQUENCE pathwheel.seq_wheelchair
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pathwheel.seq_wheelchair OWNER TO pathwheel;

--
-- TOC entry 289 (class 1259 OID 18570)
-- Name: smart_device; Type: TABLE; Schema: pathwheel; Owner: pathwheel
--

CREATE TABLE pathwheel.smart_device (
    id integer DEFAULT nextval('pathwheel.seq_smart_device'::regclass) NOT NULL,
    description character varying NOT NULL
);


ALTER TABLE pathwheel.smart_device OWNER TO pathwheel;

--
-- TOC entry 290 (class 1259 OID 18577)
-- Name: spot; Type: TABLE; Schema: pathwheel; Owner: pathwheel
--

CREATE TABLE pathwheel.spot (
    id bigint DEFAULT nextval('pathwheel.seq_spot'::regclass) NOT NULL,
    spot_type_id integer,
    registration_date timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    latitude numeric NOT NULL,
    longitude numeric NOT NULL,
    comment character varying,
    user_id bigint,
    exclusion_date timestamp without time zone,
    picture bytea,
    update_date timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    travel_mode_id integer
);


ALTER TABLE pathwheel.spot OWNER TO pathwheel;

--
-- TOC entry 301 (class 1259 OID 24634)
-- Name: spot_report; Type: TABLE; Schema: pathwheel; Owner: pathwheel
--

CREATE TABLE pathwheel.spot_report (
    spot_id bigint NOT NULL,
    user_id bigint NOT NULL,
    report_date timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    spot_report_type_id integer NOT NULL
);


ALTER TABLE pathwheel.spot_report OWNER TO pathwheel;

--
-- TOC entry 300 (class 1259 OID 24626)
-- Name: spot_report_type; Type: TABLE; Schema: pathwheel; Owner: pathwheel
--

CREATE TABLE pathwheel.spot_report_type (
    id integer NOT NULL,
    description character varying NOT NULL
);


ALTER TABLE pathwheel.spot_report_type OWNER TO pathwheel;

--
-- TOC entry 291 (class 1259 OID 18585)
-- Name: spot_type; Type: TABLE; Schema: pathwheel; Owner: pathwheel
--

CREATE TABLE pathwheel.spot_type (
    id integer NOT NULL,
    description character varying NOT NULL
);


ALTER TABLE pathwheel.spot_type OWNER TO pathwheel;

--
-- TOC entry 299 (class 1259 OID 24616)
-- Name: travel_mode; Type: TABLE; Schema: pathwheel; Owner: postgres
--

CREATE TABLE pathwheel.travel_mode (
    id integer NOT NULL,
    description character varying NOT NULL
);


ALTER TABLE pathwheel.travel_mode OWNER TO postgres;

--
-- TOC entry 292 (class 1259 OID 18591)
-- Name: user; Type: TABLE; Schema: pathwheel; Owner: pathwheel
--

CREATE TABLE pathwheel."user" (
    id bigint DEFAULT nextval('pathwheel.seq_user'::regclass) NOT NULL,
    full_name character varying NOT NULL,
    email character varying,
    username character varying,
    secret character varying NOT NULL,
    registration_date timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    exclusion_date timestamp without time zone,
    total_distance numeric,
    spot_count bigint DEFAULT 0 NOT NULL,
    fame numeric
);


ALTER TABLE pathwheel."user" OWNER TO pathwheel;

--
-- TOC entry 298 (class 1259 OID 24610)
-- Name: user_achievement; Type: TABLE; Schema: pathwheel; Owner: pathwheel
--

CREATE TABLE pathwheel.user_achievement (
);


ALTER TABLE pathwheel.user_achievement OWNER TO pathwheel;

--
-- TOC entry 293 (class 1259 OID 18599)
-- Name: wheelchair; Type: TABLE; Schema: pathwheel; Owner: pathwheel
--

CREATE TABLE pathwheel.wheelchair (
    id integer DEFAULT nextval('pathwheel.seq_wheelchair'::regclass) NOT NULL,
    description character varying
);


ALTER TABLE pathwheel.wheelchair OWNER TO pathwheel;

--
-- TOC entry 4204 (class 0 OID 0)
-- Dependencies: 293
-- Name: TABLE wheelchair; Type: COMMENT; Schema: pathwheel; Owner: pathwheel
--

COMMENT ON TABLE pathwheel.wheelchair IS 'APAGAR DEPOIS DO MESTRADO!!!!';


--
-- TOC entry 4003 (class 2606 OID 18607)
-- Name: log_authentication pk_log_authentication; Type: CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.log_authentication
    ADD CONSTRAINT pk_log_authentication PRIMARY KEY (id);


--
-- TOC entry 4007 (class 2606 OID 18609)
-- Name: pavement_sample pk_pavement_sample; Type: CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.pavement_sample
    ADD CONSTRAINT pk_pavement_sample PRIMARY KEY (id);


--
-- TOC entry 4017 (class 2606 OID 18617)
-- Name: pavement_type pk_pavement_type; Type: CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.pavement_type
    ADD CONSTRAINT pk_pavement_type PRIMARY KEY (id);


--
-- TOC entry 4019 (class 2606 OID 18619)
-- Name: smart_device pk_smart_device; Type: CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.smart_device
    ADD CONSTRAINT pk_smart_device PRIMARY KEY (id);


--
-- TOC entry 4024 (class 2606 OID 18621)
-- Name: spot pk_spot; Type: CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.spot
    ADD CONSTRAINT pk_spot PRIMARY KEY (id);


--
-- TOC entry 4042 (class 2606 OID 24639)
-- Name: spot_report pk_spot_report; Type: CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.spot_report
    ADD CONSTRAINT pk_spot_report PRIMARY KEY (spot_id, user_id, spot_report_type_id);


--
-- TOC entry 4038 (class 2606 OID 24633)
-- Name: spot_report_type pk_spot_report_type; Type: CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.spot_report_type
    ADD CONSTRAINT pk_spot_report_type PRIMARY KEY (id);


--
-- TOC entry 4026 (class 2606 OID 18623)
-- Name: spot_type pk_spot_type; Type: CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.spot_type
    ADD CONSTRAINT pk_spot_type PRIMARY KEY (id);


--
-- TOC entry 4036 (class 2606 OID 24623)
-- Name: travel_mode pk_travel_mode; Type: CONSTRAINT; Schema: pathwheel; Owner: postgres
--

ALTER TABLE ONLY pathwheel.travel_mode
    ADD CONSTRAINT pk_travel_mode PRIMARY KEY (id);


--
-- TOC entry 4028 (class 2606 OID 18625)
-- Name: user pk_user; Type: CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel."user"
    ADD CONSTRAINT pk_user PRIMARY KEY (id);


--
-- TOC entry 4034 (class 2606 OID 18627)
-- Name: wheelchair pk_wheelchair; Type: CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.wheelchair
    ADD CONSTRAINT pk_wheelchair PRIMARY KEY (id);


--
-- TOC entry 4030 (class 2606 OID 18629)
-- Name: user uq_user_email; Type: CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel."user"
    ADD CONSTRAINT uq_user_email UNIQUE (email);


--
-- TOC entry 4032 (class 2606 OID 18631)
-- Name: user uq_user_username; Type: CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel."user"
    ADD CONSTRAINT uq_user_username UNIQUE (username);


--
-- TOC entry 4001 (class 1259 OID 18632)
-- Name: fki_log_authentication_user_id; Type: INDEX; Schema: pathwheel; Owner: pathwheel
--

CREATE INDEX fki_log_authentication_user_id ON pathwheel.log_authentication USING btree (user_id);


--
-- TOC entry 4004 (class 1259 OID 24683)
-- Name: fki_pavement_sample_travel_mode_id; Type: INDEX; Schema: pathwheel; Owner: pathwheel
--

CREATE INDEX fki_pavement_sample_travel_mode_id ON pathwheel.pavement_sample USING btree (travel_mode_id);


--
-- TOC entry 4005 (class 1259 OID 18633)
-- Name: fki_pavement_sample_user_id; Type: INDEX; Schema: pathwheel; Owner: pathwheel
--

CREATE INDEX fki_pavement_sample_user_id ON pathwheel.pavement_sample USING btree (user_id);


--
-- TOC entry 4039 (class 1259 OID 24656)
-- Name: fki_spot_report_report_type_id; Type: INDEX; Schema: pathwheel; Owner: pathwheel
--

CREATE INDEX fki_spot_report_report_type_id ON pathwheel.spot_report USING btree (spot_report_type_id);


--
-- TOC entry 4040 (class 1259 OID 24650)
-- Name: fki_spot_report_user_id; Type: INDEX; Schema: pathwheel; Owner: pathwheel
--

CREATE INDEX fki_spot_report_user_id ON pathwheel.spot_report USING btree (user_id);


--
-- TOC entry 4020 (class 1259 OID 24689)
-- Name: fki_spot_travel_mode_id; Type: INDEX; Schema: pathwheel; Owner: pathwheel
--

CREATE INDEX fki_spot_travel_mode_id ON pathwheel.spot USING btree (travel_mode_id);


--
-- TOC entry 4021 (class 1259 OID 18639)
-- Name: fki_spot_type_id; Type: INDEX; Schema: pathwheel; Owner: pathwheel
--

CREATE INDEX fki_spot_type_id ON pathwheel.spot USING btree (spot_type_id);


--
-- TOC entry 4022 (class 1259 OID 18640)
-- Name: fki_user_id; Type: INDEX; Schema: pathwheel; Owner: pathwheel
--

CREATE INDEX fki_user_id ON pathwheel.spot USING btree (user_id);


--
-- TOC entry 4045 (class 2606 OID 18641)
-- Name: log_authentication fk_log_authentication_user_id; Type: FK CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.log_authentication
    ADD CONSTRAINT fk_log_authentication_user_id FOREIGN KEY (user_id) REFERENCES pathwheel."user"(id);


--
-- TOC entry 4046 (class 2606 OID 24678)
-- Name: pavement_sample fk_pavement_sample_travel_mode_id; Type: FK CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.pavement_sample
    ADD CONSTRAINT fk_pavement_sample_travel_mode_id FOREIGN KEY (travel_mode_id) REFERENCES pathwheel.travel_mode(id);


--
-- TOC entry 4047 (class 2606 OID 18646)
-- Name: pavement_sample fk_pavement_sample_user_id; Type: FK CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.pavement_sample
    ADD CONSTRAINT fk_pavement_sample_user_id FOREIGN KEY (user_id) REFERENCES pathwheel."user"(id);


--
-- TOC entry 4057 (class 2606 OID 24651)
-- Name: spot_report fk_spot_report_report_type_id; Type: FK CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.spot_report
    ADD CONSTRAINT fk_spot_report_report_type_id FOREIGN KEY (spot_report_type_id) REFERENCES pathwheel.spot_report_type(id);


--
-- TOC entry 4055 (class 2606 OID 24640)
-- Name: spot_report fk_spot_report_spot_id; Type: FK CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.spot_report
    ADD CONSTRAINT fk_spot_report_spot_id FOREIGN KEY (spot_id) REFERENCES pathwheel.spot(id);


--
-- TOC entry 4056 (class 2606 OID 24645)
-- Name: spot_report fk_spot_report_user_id; Type: FK CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.spot_report
    ADD CONSTRAINT fk_spot_report_user_id FOREIGN KEY (user_id) REFERENCES pathwheel."user"(id);


--
-- TOC entry 4052 (class 2606 OID 24684)
-- Name: spot fk_spot_travel_mode_id; Type: FK CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.spot
    ADD CONSTRAINT fk_spot_travel_mode_id FOREIGN KEY (travel_mode_id) REFERENCES pathwheel.travel_mode(id);


--
-- TOC entry 4053 (class 2606 OID 18676)
-- Name: spot fk_spot_type_id; Type: FK CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.spot
    ADD CONSTRAINT fk_spot_type_id FOREIGN KEY (spot_type_id) REFERENCES pathwheel.spot_type(id);


--
-- TOC entry 4054 (class 2606 OID 18681)
-- Name: spot fk_user_id; Type: FK CONSTRAINT; Schema: pathwheel; Owner: pathwheel
--

ALTER TABLE ONLY pathwheel.spot
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES pathwheel."user"(id);


-- Completed on 2019-10-16 17:22:39 BRT

--
-- PostgreSQL database dump complete
--

