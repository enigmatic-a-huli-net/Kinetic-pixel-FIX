package net.mcreator.kineticpixelfix.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.mcreator.kineticpixelfix.init.KineticPixelFixModItems;

public class WildGrayCottonBlock extends CropBlock {
	public static final MapCodec<WildGrayCottonBlock> CODEC = simpleCodec(WildGrayCottonBlock::new);
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			box(2, 0, 2, 14, 3, 14),
			box(2, 0, 2, 14, 5, 14),
			box(2, 0, 2, 14, 8, 14),
			box(2, 0, 2, 14, 12, 14)
	};

	public WildGrayCottonBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0));
	}

	@Override
	public MapCodec<? extends CropBlock> codec() {
		return CODEC;
	}

	@Override
	public IntegerProperty getAgeProperty() {
		return AGE;
	}

	@Override
	public int getMaxAge() {
		return 3;
	}

	@Override
	protected Item getBaseSeedId() {
		return KineticPixelFixModItems.GRAY_COTTON_SEEDS.get();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE_BY_AGE[this.getAge(state)];
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(net.minecraft.world.level.block.Blocks.FARMLAND)
				|| state.is(net.minecraft.world.level.block.Blocks.GRASS_BLOCK)
				|| state.is(net.minecraft.world.level.block.Blocks.DIRT)
				|| state.is(net.minecraft.world.level.block.Blocks.PODZOL);
	}

	@Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
            Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide() && !canSurvive(state, level, pos)) {
            dropResources(state, level, pos); // стандартный метод Block — вызовет лут-таблицу
            level.removeBlock(pos, false);
        }
        // НЕ вызываем super — иначе ванильный CropBlock удалит блок без дропа второй раз
    }
}