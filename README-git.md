# Git subtree
[tutorial](https://www.atlassian.com/git/tutorials/git-subtree)

## Init
`git remote add -f reveal.js https://github.com/hakimel/reveal.js.git`
`git subtree add --prefix presentation-2024-kotest-vs-junit/ https://github.com/hakimel/reveal.js.git master`

## update
```bash
git fetch reveal.js master
git subtree pull --prefix presentation-2024-kotest-vs-junit/ reveal.js master
```