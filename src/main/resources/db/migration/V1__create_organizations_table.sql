CREATE TABLE organizations (
                               id UUID PRIMARY KEY,

                               name VARCHAR(150) NOT NULL,

                               slug VARCHAR(100) NOT NULL,

                               status VARCHAR(30) NOT NULL
                                   DEFAULT 'ACTIVE',

                               created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,

                               updated_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,

                               CONSTRAINT uk_organization_slug
                                   UNIQUE (slug),

                               CONSTRAINT chk_organization_status
                                   CHECK (
                                       status IN (
                                                  'ACTIVE',
                                                  'SUSPENDED',
                                                  'DEACTIVATED'
                                           )
                                       )
);