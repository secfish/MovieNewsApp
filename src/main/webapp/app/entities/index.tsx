import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Movie from './movie';
import News from './news';
//import Twitter from './twitter';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}movie`} component={Movie} />
      <ErrorBoundaryRoute path={`${match.url}news`} component={News} />
      {/*<ErrorBoundaryRoute path={`${match.url}twitter`} component={Twitter} />*/}
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
