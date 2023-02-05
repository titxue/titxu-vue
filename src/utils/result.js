export function successResult(result, { message = 'Request success' } = {}) {
  return {
    code: 200,
    result,
    message,
    status: 'ok',
  };
}
export function okResult({ msg = 'Request success' } = {}) {
  return {
    code: 200,
    msg,
    status: 'ok',
  };
}
export function errorResult(message = 'Request failed', { code = -1, result = null } = {}) {
  return {
    code,
    result,
    message,
    status: 'fail',
  };
}
export function pageSuccessResult(page, pageSize, list, { message = 'ok' } = {}) {
  const pageData = pagination(page, pageSize, list);
  return {
    ...successResult({
      items: pageData,
      total: list.length,
    }),
    message,
  };
}
export function pagination(pageNo, pageSize, array) {
  const offset = (pageNo - 1) * Number(pageSize);
  const res =
    offset + Number(pageSize) >= array.length ? array.slice(offset, array.length) : array.slice(offset, offset + Number(pageSize));
  return res;
}
export function getRequestToken({ headers }) {
  return headers?.authorization;
}
//# sourceMappingURL=result.js.map
