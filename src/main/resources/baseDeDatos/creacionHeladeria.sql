CREATE SEQUENCE public.sabores_id_seq
    INCREMENT 1
    START 100
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE 1;

ALTER SEQUENCE public.sabores_id_seq
    OWNER TO postgres;

    -- Table: public.sabores

-- DROP TABLE public.sabores;

CREATE TABLE public.sabores
(
    id integer NOT NULL DEFAULT nextval('sabores_id_seq'::regclass),
    nombre text COLLATE pg_catalog."default" NOT NULL,
    tipo text COLLATE pg_catalog."default" NOT NULL,
    descripcion text COLLATE pg_catalog."default",
    stock integer NOT NULL,
    apto_celiacos boolean NOT NULL,
    CONSTRAINT sabores_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.sabores
    OWNER to postgres;

    