
CREATE TABLE public.user_registered
(
    id uuid NOT NULL,
    created timestamp without time zone,
    email character varying(255),
    is_active boolean,
    last_login timestamp without time zone,
    name character varying(255) ,
    password character varying(255),
    token character varying,
    CONSTRAINT user_registered_pkey PRIMARY KEY (id),
    CONSTRAINT user_email UNIQUE (email)
);


CREATE TABLE public.phone
(
    id int8 NOT NULL DEFAULT nextval('phone_sequence'),
    city_code integer,
    country_code character varying(255) ,
    number integer,
    user_registered_id uuid,
    CONSTRAINT phone_pkey PRIMARY KEY (id)
    --CONSTRAINT phone_user_registered_key FOREIGN KEY (user_registered_id) REFERENCES public.user_registered (id)
);


