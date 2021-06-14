import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './movie.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMovieDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MovieDetail = (props: IMovieDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { movieEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="movieDetailsHeading">
          <Translate contentKey="movieNewsApp.movie.detail.title">Movie</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{movieEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="movieNewsApp.movie.name">Name</Translate>
            </span>
          </dt>
          <dd>{movieEntity.name}</dd>
          <dt>
            <span id="director">
              <Translate contentKey="movieNewsApp.movie.director">Director</Translate>
            </span>
          </dt>
          <dd>{movieEntity.director}</dd>
          <dt>
            <span id="synopsis">
              <Translate contentKey="movieNewsApp.movie.synopsis">Synopsis</Translate>
            </span>
          </dt>
          <dd>{movieEntity.synopsis}</dd>
          <dt>
            <span id="comment">
              <Translate contentKey="movieNewsApp.movie.comment">Comment</Translate>
            </span>
          </dt>
          <dd>{movieEntity.comment}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="movieNewsApp.movie.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>{movieEntity.startDate ? <TextFormat value={movieEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="movieNewsApp.movie.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {movieEntity.image ? (
              <div>
                {movieEntity.imageContentType ? (
                  <a onClick={openFile(movieEntity.imageContentType, movieEntity.image)}>
                    <img src={`data:${movieEntity.imageContentType};base64,${movieEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {movieEntity.imageContentType}, {byteSize(movieEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="movieNewsApp.movie.user">User</Translate>
          </dt>
          <dd>{movieEntity.user ? movieEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/movie" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/movie/${movieEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ movie }: IRootState) => ({
  movieEntity: movie.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MovieDetail);
