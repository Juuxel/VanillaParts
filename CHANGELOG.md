# Changelog

Based on the [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) format.
Since this isn't a library, doesn't follow Semantic Versioning,
but major versions still indicate breaking changes to worlds etc.

## Unreleased
### Added
- `#vanilla_parts:excluded` block tag
  for excluding blocks from becoming parts
- Soul torch parts
- Part categories for all parts in the `vanilla_parts` namespace (via Blocks to Parts):
  
  | Part       | Categories                       |
  |------------|----------------------------------|
  | Button     | `buttons`, `redstone_components` |
  | Cake       | `cake`                           |
  | Carpet     | `carpets`                        |
  | Fence      | `fences`                         |
  | Lever      | `levers`, `redstone_components`  |
  | Slab       | `slabs`                          |
  | Soul torch | `torches`                        |
  | Torch      | `torches`                        |

### Changed
- Many generic parts of the implementation
  have been moved to a separate library,
  [Blocks to Parts](https://github.com/Juuxel/BlocksToParts).
- Updated to Minecraft 1.18.1.
- Reduced the amount of mixins dramatically thanks to LibMultiPart updates.

### Fixed
- Fix torch luminance being 15 instead of 14
