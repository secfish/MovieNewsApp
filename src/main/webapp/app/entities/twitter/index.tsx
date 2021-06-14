import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Twitter from './twitter';
import TwitterDetail from './twitter-detail';
import TwitterUpdate from './twitter-update';
import TwitterDeleteDialog from './twitter-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TwitterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TwitterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TwitterDetail} />
      <ErrorBoundaryRoute path={match.url} component={Twitter} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TwitterDeleteDialog} />
  </>
);

export default Routes;
