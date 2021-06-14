import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './twitter.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITwitterDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TwitterDetail = (props: ITwitterDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { twitterEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="twitterDetailsHeading">
          <Translate contentKey="movieNewsApp.twitter.detail.title">Twitter</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{twitterEntity.id}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="movieNewsApp.twitter.content">Content</Translate>
            </span>
          </dt>
          <dd>{twitterEntity.content}</dd>
          <dt>
            <span id="pubDate">
              <Translate contentKey="movieNewsApp.twitter.pubDate">Pub Date</Translate>
            </span>
          </dt>
          <dd>{twitterEntity.pubDate ? <TextFormat value={twitterEntity.pubDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="publisher">
              <Translate contentKey="movieNewsApp.twitter.publisher">Publisher</Translate>
            </span>
          </dt>
          <dd>{twitterEntity.publisher}</dd>
          <dt>
            <Translate contentKey="movieNewsApp.twitter.movie">Movie</Translate>
          </dt>
          <dd>{twitterEntity.movie ? twitterEntity.movie.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/twitter" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/twitter/${twitterEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ twitter }: IRootState) => ({
  twitterEntity: twitter.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TwitterDetail);
