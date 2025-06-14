-- DROP SCHEMA public;

CREATE SCHEMA public AUTHORIZATION marcosalencar;

-- DROP SCHEMA public;

CREATE SCHEMA public AUTHORIZATION marcosalencar;

-- Drop table

-- DROP TABLE public.sector;

CREATE TABLE public.sector (
	sector bpchar(1) NOT NULL,
	base_price float8 NOT NULL,
	currency varchar(255) NOT NULL,
	max_capacity int4 NOT NULL,
	open_hour time NOT NULL,
	close_hour time NOT NULL,
	duration_limit_minutes int4 NOT NULL,
	current_occupancy int4 DEFAULT 0 NOT NULL,
	CONSTRAINT sector_pkey PRIMARY KEY (sector)
);

-- Drop table

-- DROP TABLE public.spot;

CREATE TABLE public.spot (
	id_spot bigserial NOT NULL,
	sector bpchar(1) NOT NULL,
	lat numeric(10, 6) NOT NULL,
	lng numeric(10, 6) NOT NULL,
	occupied bool DEFAULT false NOT NULL,
	CONSTRAINT spot_pkey PRIMARY KEY (id_spot),
	CONSTRAINT spot_sector_fkey FOREIGN KEY (sector) REFERENCES public.sector(sector) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Drop table

-- DROP TABLE public.spot_occupancy;

CREATE TABLE public.spot_occupancy (
	id_occupancy bigserial NOT NULL,
	license_plate varchar(255) NOT NULL,
	entry_time timestamp DEFAULT now() NOT NULL,
	id_spot int8 NULL,
	price_per_hour float8 NULL,
	exit_time timestamp NULL,
	final_price float8 NULL,
	CONSTRAINT spot_occupancy_pkey PRIMARY KEY (id_occupancy),
	CONSTRAINT spot_occupancy_id_spot_fkey FOREIGN KEY (id_spot) REFERENCES public.spot(id_spot) ON DELETE RESTRICT ON UPDATE CASCADE
);