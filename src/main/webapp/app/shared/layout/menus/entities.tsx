import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/tenant-type">
      Tenant Type
    </MenuItem>
    <MenuItem icon="asterisk" to="/tenant">
      Tenant
    </MenuItem>
    <MenuItem icon="asterisk" to="/url-type">
      URL Type
    </MenuItem>
    <MenuItem icon="asterisk" to="/url">
      URL
    </MenuItem>
    <MenuItem icon="asterisk" to="/tenant-property-key">
      Tenant Property Key
    </MenuItem>
    <MenuItem icon="asterisk" to="/tenant-property">
      Tenant Property
    </MenuItem>
    <MenuItem icon="asterisk" to="/lan-user">
      Lan User
    </MenuItem>
    <MenuItem icon="asterisk" to="/tenant-user">
      Tenant User
    </MenuItem>
    <MenuItem icon="asterisk" to="/work-template">
      Work Template
    </MenuItem>
    <MenuItem icon="asterisk" to="/work-template-item">
      Work Template Item
    </MenuItem>
    <MenuItem icon="asterisk" to="/work-template-item-pre-req">
      Work Template Item Pre Req
    </MenuItem>
    <MenuItem icon="asterisk" to="/work-queue-tenant">
      Work Queue Tenant
    </MenuItem>
    <MenuItem icon="asterisk" to="/work-queue-tenant-data">
      Work Queue Tenant Data
    </MenuItem>
    <MenuItem icon="asterisk" to="/work-queue-tenant-pre-req">
      Work Queue Tenant Pre Req
    </MenuItem>
    <MenuItem icon="asterisk" to="/work-queue-tenant-user">
      Work Queue Tenant User
    </MenuItem>
    <MenuItem icon="asterisk" to="/work-queue-tenant-user-data">
      Work Queue Tenant User Data
    </MenuItem>
    <MenuItem icon="asterisk" to="/work-queue-tenant-user-pre-req">
      Work Queue Tenant User Pre Req
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
