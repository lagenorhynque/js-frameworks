import hello from './hello';
import ReactDom from 'react-dom';

hello();

ReactDom.render(
  <h1>Hello, Frontend Engineer!</h1>,
  document.getElementById('root')
);
