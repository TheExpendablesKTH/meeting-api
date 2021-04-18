--
-- PostgreSQL database dump
--

-- Dumped from database version 13.2
-- Dumped by pg_dump version 13.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: admins; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.admins (
    id integer NOT NULL,
    name character varying
);


ALTER TABLE public.admins OWNER TO postgres;

--
-- Name: admins_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.admins_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.admins_id_seq OWNER TO postgres;

--
-- Name: admins_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.admins_id_seq OWNED BY public.admins.id;


--
-- Name: calls_attendees; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.calls_attendees (
    id integer NOT NULL,
    call_id integer NOT NULL,
    attendee_id character varying NOT NULL,
    join_token character varying NOT NULL,
    resident_id integer,
    relative_id integer
);


ALTER TABLE public.calls_attendees OWNER TO postgres;

--
-- Name: call_attendees_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.call_attendees_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.call_attendees_id_seq OWNER TO postgres;

--
-- Name: call_attendees_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.call_attendees_id_seq OWNED BY public.calls_attendees.id;


--
-- Name: calls; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.calls (
    id integer NOT NULL,
    meeting_id character varying NOT NULL,
    audio_host_url character varying NOT NULL,
    audio_fallback_url character varying NOT NULL,
    screen_data_url character varying NOT NULL,
    screen_sharing_url character varying NOT NULL,
    screen_viewing_url character varying NOT NULL,
    signaling_url character varying NOT NULL,
    turn_control_url character varying NOT NULL
);


ALTER TABLE public.calls OWNER TO postgres;

--
-- Name: calls_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.calls_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.calls_id_seq OWNER TO postgres;

--
-- Name: calls_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.calls_id_seq OWNED BY public.calls.id;


--
-- Name: calls_invites; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.calls_invites (
    id integer NOT NULL,
    calls_attendees_id integer NOT NULL,
    code character varying NOT NULL
);


ALTER TABLE public.calls_invites OWNER TO postgres;

--
-- Name: calls_invites_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.calls_invites_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.calls_invites_id_seq OWNER TO postgres;

--
-- Name: calls_invites_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.calls_invites_id_seq OWNED BY public.calls_invites.id;


--
-- Name: devices; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.devices (
    id integer NOT NULL,
    residency_id integer NOT NULL
);


ALTER TABLE public.devices OWNER TO postgres;

--
-- Name: devices_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.devices_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.devices_id_seq OWNER TO postgres;

--
-- Name: devices_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.devices_id_seq OWNED BY public.devices.id;


--
-- Name: identities_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.identities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.identities_id_seq OWNER TO postgres;

--
-- Name: identities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.identities (
    id integer DEFAULT nextval('public.identities_id_seq'::regclass) NOT NULL,
    type character varying NOT NULL,
    residency_id integer NOT NULL,
    username character varying NOT NULL,
    password character varying,
    admin_id integer,
    device_id integer,
    resident_id integer
);


ALTER TABLE public.identities OWNER TO postgres;

--
-- Name: relatives; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.relatives (
    id integer NOT NULL,
    name character varying NOT NULL,
    phone character varying NOT NULL,
    resident_id integer NOT NULL
);


ALTER TABLE public.relatives OWNER TO postgres;

--
-- Name: relatives_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.relatives_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.relatives_id_seq OWNER TO postgres;

--
-- Name: relatives_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.relatives_id_seq OWNED BY public.relatives.id;


--
-- Name: residencies; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.residencies (
    id integer NOT NULL,
    name character varying NOT NULL,
    device_passphrase character varying NOT NULL
);


ALTER TABLE public.residencies OWNER TO postgres;

--
-- Name: residencies_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.residencies_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.residencies_id_seq OWNER TO postgres;

--
-- Name: residencies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.residencies_id_seq OWNED BY public.residencies.id;


--
-- Name: residents_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.residents_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.residents_id_seq OWNER TO postgres;

--
-- Name: residents; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.residents (
    id integer DEFAULT nextval('public.residents_id_seq'::regclass) NOT NULL,
    name character varying NOT NULL,
    residency_id integer NOT NULL
);


ALTER TABLE public.residents OWNER TO postgres;

--
-- Name: admins id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admins ALTER COLUMN id SET DEFAULT nextval('public.admins_id_seq'::regclass);


--
-- Name: calls id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calls ALTER COLUMN id SET DEFAULT nextval('public.calls_id_seq'::regclass);


--
-- Name: calls_attendees id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calls_attendees ALTER COLUMN id SET DEFAULT nextval('public.call_attendees_id_seq'::regclass);


--
-- Name: calls_invites id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calls_invites ALTER COLUMN id SET DEFAULT nextval('public.calls_invites_id_seq'::regclass);


--
-- Name: devices id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devices ALTER COLUMN id SET DEFAULT nextval('public.devices_id_seq'::regclass);


--
-- Name: relatives id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relatives ALTER COLUMN id SET DEFAULT nextval('public.relatives_id_seq'::regclass);


--
-- Name: residencies id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.residencies ALTER COLUMN id SET DEFAULT nextval('public.residencies_id_seq'::regclass);


--
-- Data for Name: admins; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.admins (id, name) FROM stdin;
\.


--
-- Data for Name: calls; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.calls (id, meeting_id, audio_host_url, audio_fallback_url, screen_data_url, screen_sharing_url, screen_viewing_url, signaling_url, turn_control_url) FROM stdin;
\.


--
-- Data for Name: calls_attendees; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.calls_attendees (id, call_id, attendee_id, join_token, resident_id, relative_id) FROM stdin;
\.


--
-- Data for Name: calls_invites; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.calls_invites (id, calls_attendees_id, code) FROM stdin;
\.


--
-- Data for Name: devices; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.devices (id, residency_id) FROM stdin;
\.


--
-- Data for Name: identities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.identities (id, type, residency_id, username, password, admin_id, device_id, resident_id) FROM stdin;
\.


--
-- Data for Name: relatives; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.relatives (id, name, phone, resident_id) FROM stdin;
\.


--
-- Data for Name: residencies; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.residencies (id, name, device_passphrase) FROM stdin;
1	nalens äldreboende	hemlis
2	nytt äldreboendet	super.säkert.lösen
\.


--
-- Data for Name: residents; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.residents (id, name, residency_id) FROM stdin;
\.


--
-- Name: admins_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.admins_id_seq', 1, false);


--
-- Name: call_attendees_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.call_attendees_id_seq', 1, false);


--
-- Name: calls_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.calls_id_seq', 1, false);


--
-- Name: calls_invites_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.calls_invites_id_seq', 1, false);


--
-- Name: devices_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.devices_id_seq', 1, false);


--
-- Name: identities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.identities_id_seq', 8, true);


--
-- Name: relatives_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.relatives_id_seq', 1, false);


--
-- Name: residencies_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.residencies_id_seq', 1, false);


--
-- Name: residents_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.residents_id_seq', 6, true);


--
-- Name: admins admins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admins
    ADD CONSTRAINT admins_pkey PRIMARY KEY (id);


--
-- Name: calls_attendees call_attendees_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calls_attendees
    ADD CONSTRAINT call_attendees_pkey PRIMARY KEY (id);


--
-- Name: calls_invites calls_invites_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calls_invites
    ADD CONSTRAINT calls_invites_pkey PRIMARY KEY (id);


--
-- Name: calls calls_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calls
    ADD CONSTRAINT calls_pkey PRIMARY KEY (id);


--
-- Name: devices devices_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devices
    ADD CONSTRAINT devices_pkey PRIMARY KEY (id);


--
-- Name: identities identities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.identities
    ADD CONSTRAINT identities_pkey PRIMARY KEY (id);


--
-- Name: relatives relatives_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relatives
    ADD CONSTRAINT relatives_pkey PRIMARY KEY (id);


--
-- Name: residencies residencies_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.residencies
    ADD CONSTRAINT residencies_pkey PRIMARY KEY (id);


--
-- Name: residents residents_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.residents
    ADD CONSTRAINT residents_pkey PRIMARY KEY (id);


--
-- Name: identities_username_unique; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX identities_username_unique ON public.identities USING btree (username);


--
-- Name: residencies_passphrase_unique; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX residencies_passphrase_unique ON public.residencies USING btree (device_passphrase);


--
-- Name: calls_attendees calls_attendees_call_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calls_attendees
    ADD CONSTRAINT calls_attendees_call_id_fkey FOREIGN KEY (call_id) REFERENCES public.calls(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: calls_attendees calls_attendees_relative_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calls_attendees
    ADD CONSTRAINT calls_attendees_relative_id_fkey FOREIGN KEY (relative_id) REFERENCES public.relatives(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: calls_attendees calls_attendees_resident_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calls_attendees
    ADD CONSTRAINT calls_attendees_resident_id_fkey FOREIGN KEY (resident_id) REFERENCES public.residents(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: calls_invites calls_invites_calls_attendees_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.calls_invites
    ADD CONSTRAINT calls_invites_calls_attendees_id_fkey FOREIGN KEY (calls_attendees_id) REFERENCES public.calls_attendees(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: devices devices_residency_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.devices
    ADD CONSTRAINT devices_residency_id_fkey FOREIGN KEY (residency_id) REFERENCES public.residencies(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: identities identities_admin_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.identities
    ADD CONSTRAINT identities_admin_id_fkey FOREIGN KEY (admin_id) REFERENCES public.admins(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: identities identities_device_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.identities
    ADD CONSTRAINT identities_device_id_fkey FOREIGN KEY (device_id) REFERENCES public.devices(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: identities identities_residency_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.identities
    ADD CONSTRAINT identities_residency_id_fkey FOREIGN KEY (residency_id) REFERENCES public.residencies(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: identities identities_resident_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.identities
    ADD CONSTRAINT identities_resident_id_fkey FOREIGN KEY (resident_id) REFERENCES public.residents(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: relatives relatives_resident_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.relatives
    ADD CONSTRAINT relatives_resident_id_fkey FOREIGN KEY (resident_id) REFERENCES public.residents(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: residents residents_residency_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.residents
    ADD CONSTRAINT residents_residency_id_fkey FOREIGN KEY (residency_id) REFERENCES public.residencies(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

