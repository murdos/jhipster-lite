module.exports = {
  parser: '@typescript-eslint/parser',
  extends: ['eslint:recommended', '@vue/eslint-config-typescript', 'prettier', 'plugin:cypress/recommended'],
  root: true,
  plugins: ['cypress', '@typescript-eslint'],
  env: {
    node: true,
    mocha: true,
    'cypress/globals': true,
  },
};
