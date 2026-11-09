CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       full_name VARCHAR(150) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
                       created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
                       updated_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,

                       CONSTRAINT uk_users_email UNIQUE (email),
                       CONSTRAINT chk_users_status
                           CHECK (status IN ('ACTIVE', 'DISABLED'))
);

CREATE TABLE organization_memberships (
                                          id UUID PRIMARY KEY,
                                          user_id UUID NOT NULL,
                                          organization_id UUID NOT NULL,
                                          role VARCHAR(30) NOT NULL,
                                          status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
                                          created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,

                                          CONSTRAINT fk_membership_user
                                              FOREIGN KEY (user_id) REFERENCES users(id),

                                          CONSTRAINT fk_membership_organization
                                              FOREIGN KEY (organization_id) REFERENCES organizations(id),

                                          CONSTRAINT uk_membership_user_organization
                                              UNIQUE (user_id, organization_id),

                                          CONSTRAINT chk_membership_role
                                              CHECK (role IN ('OWNER', 'ADMIN', 'MEMBER')),

                                          CONSTRAINT chk_membership_status
                                              CHECK (status IN ('ACTIVE', 'INVITED', 'SUSPENDED'))
);

CREATE INDEX idx_membership_organization
    ON organization_memberships(organization_id);